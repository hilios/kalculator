# RPN Calculator

### A command line interface RPN calculator.

## Build & Run

```bash
$ gradle test jar
BUILD SUCCESSFUL in 954ms
5 actionable tasks: 2 executed, 3 up-to-date
$ java -jar build/libs/kalculator-0.1.jar
```

## Examples

```
 _     __   _    __   _    _     __ ________  ___
| |_/ / /\ | |  / /` | | || |   / /\ | |/ / \| |_)
|_| \/_/--\|_|__\_\_,\_\_/|_|__/_/--\|_|\_\_/|_| \

A command line RPN calculator.

Operations: +, -, *, /, sqrt, clean, undo
Exit: Ctrl + C

> 5 2
stack: 5 2

> 2 sqrt
stack: 1.4142135624
> clear 9 sqrt
stack: 3


> 5 2 -
stack: 3
> 3 -
stack: 0
> clear
stack:


> 5 4 3 2
stack: 5 4 3 2
> undo undo *
stack: 20
> 5 *
stack: 100
> undo
stack: 20 5


> 7 12 2 /
stack: 7 6
> *
stack: 42
> 4 /
stack: 10.5


> 1 2 3 4 5
stack: 1 2 3 4 5
> *
stack: 1 2 3 20
> clear
stack:
> 3 4 -
stack: -1


> 1 2 3 4 5
stack: 1 2 3 4 5
> * * * *
stack: 120


> 1 2 3 * 5 + * * 6 5
operator * (position: 15): insufficient parameters
stack: 11
```