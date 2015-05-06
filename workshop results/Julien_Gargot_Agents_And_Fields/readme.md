# agents_and_fields

###### 20150419 | Processing Paris | Julien Gargot


---

## Purpose

Generates a matrix of force fields like a rectangular grid.
Agents walk upon it.
Multiple dynamic parameters.

#### Dynamic parameters

| name                    | type   | default value | min/max           | role                                                           |
|-------------------------|--------|---------------|-------------------|----------------------------------------------------------------|
| gridStepping            | int    | 2             | 1/32              | Sets the number of columns and rows of the field matrix.       |
| agentCount              | int    | 1             | 1/2048            | Number of agent.                                               |
| agentSize               | float  | 1             | 1/64              | Size of agent.                                                 |
| agentStepSize           | float  | 5             | 1/32              | Agent speed.                                                   |
| refreshFieldPeriodicity | int    | 20            | 1/30              | Field refresh period.                                          |
| fieldOrientation        | float  | 0             | 0/2*PI            | Orientation of the force field. More revelant on "breath" mode |
| refreshAlpha            | float  | 20            | 0/255             | Alpha refresh rate of the scene.                               |
| mode                    | String | "random"      | "random"/"breath" | Defines the behavior of changing force fields.                 |

#### Settings

| name            | type  | default value | min/max | role                                                       |
|-----------------|-------|---------------|---------|------------------------------------------------------------|
| agentColor      | color | color(255)    |         | Agent color.                                               |
| agentAlpha      | float | 90            | 0/255   | Agent alpha.                                               |
| fieldAlpha      | float | 128           | 0,255   | Sets the initial value of the force field through a color. |
| backgroundColor | color | color(0)      |         | Background color.                                          |

##### TODO

Connect it to the library of Laurent.