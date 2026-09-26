# PScript

Un compilador pequeño, hecho a mano: toma código de un lenguaje propio y
genera C. Es el proyecto de Lenguajes y Autómatas II del Instituto
Tecnológico de Jiquilpan (2021), construido siguiendo el Aho-Sethi-Ullman.

No es un intérprete ni un juguete de expresiones: recorre las cuatro fases
de un compilador de verdad.

| Fase | Dónde | Qué hace |
|---|---|---|
| Léxica | `Lexer.java`, `Tokens.java` | Reconoce los tokens con expresiones regulares y llena la tabla de símbolos |
| Sintáctica | `Parser.java`, `Tablas.java` | Analizador **LR** con tablas de desplazamiento y reducción |
| Semántica | `Semantic.java` | Comprueba tipos en asignaciones y expresiones |
| Generación | `Parser.java` | Emite C equivalente: `int`/`float`/`char`, `printf`, `scanf` y saltos |

El IDE (`src/editor`, JavaFX + RichTextFX) trae resaltado de sintaxis, la
consola de errores con número de línea y el panel del C generado.

## El lenguaje

```
programa
  ent @contador;
  ent @tope;
  lec @tope;

  @contador = 0;
  mientras @contador < @tope hacer
  inicio
    imp @contador;
    @contador = @contador + 1;
  fin
```

- Variables con `@`, tipos `ent`, `dec` y `cart`.
- `si … sino … endif`, `mientras … hacer`, bloques con `inicio`/`fin`.
- `lec` para leer y `imp` para imprimir.
- Aritmética `+ - * /` y comparaciones `< > <= >= == !=`.

## Correr

Proyecto de IntelliJ con JavaFX. Las dependencias de RichTextFX están en
`src/*.jar`. Punto de entrada: `src/editor/EditorApp.java`.

## Hay una versión en el navegador

El mismo lenguaje, reescrito en JavaScript y con un editor en la web, en
**[ayotl.dev/pscript](https://ayotl.dev/pscript)**: se escribe PScript y se
ve el C que sale, los tokens y los errores, sin instalar nada.

---

Hecho por [Adrián Ceja Rentería](https://ayotl.dev/acerca) · [ayotl.dev](https://ayotl.dev)
