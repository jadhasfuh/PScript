package editor;

public class IOResult<T> {
    
    private T data;
    private boolean ok;
    
    public IOResult(boolean ok, T data){
        this.ok = ok;
        this.data = data;
    }
    
    public boolean isok(){
        return ok;
    }
    
    public boolean hasData(){
        return data != null;
    }
    
    public T getData(){
        return data;
    }
    
}
