package editor;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.Subscription;

import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.reactfx.EventStream;


public abstract class Highlight {

    /** Target CodeArea. */
    protected final CodeArea codeArea;

    /** ExecutorService. */
    protected final ExecutorService executor;

    /**
     * Initialize with codeArea.
     * @param codeArea
     */
    public Highlight(final CodeArea codeArea) {
        this.codeArea = codeArea;
        this.executor = Executors.newSingleThreadExecutor();
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });
        
        EventStream<?> richChanges = codeArea.richChanges();
        richChanges
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(richChanges)
                .filterMap(t -> {
                    if (t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

    }

    /**
     * Do highlighting.
     */
    public final Subscription highlight() {
        final String code = codeArea.getText();
        final Subscription subscription = codeArea.richChanges()
            .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
            .successionEnds(Duration.ofMillis(500))
            .supplyTask(this::computeHighlightingAsync)
            .awaitLatest(codeArea.richChanges())
            .filterMap(t -> {
                if(t.isSuccess()) {
                    return Optional.of(t.get());
                }
                t.getFailure().printStackTrace();
                return Optional.empty();
            })
            .subscribe(this::applyHighlighting);
        codeArea.replaceText(code);
        return subscription;
    }

    /**
     * Compute highlighting.
     * @return
     */
    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        final String text = codeArea.getText();

        final Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    /**
     * Apply highlighting.
     * @param highlighting
     */
    private void applyHighlighting(final StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    /**
     * Compute highlighting.
     * @param text
     * @return
     */
    protected abstract StyleSpans<Collection<String>> computeHighlighting(final String text);
    
}