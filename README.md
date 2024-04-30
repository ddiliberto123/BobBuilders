# BobBuilders
Bob the builders build!

---
## Introduction:
Our game, Frenzy Penguins, involves sending a penguin off a ramp through the skies. Throughout your journey, you will be able to guide the penguin through the air, accumulating points for the distance that you travel and purchasing equipemnt to stay airborne for as long as you can!
---

## How to play
Controls: Use keys to control the penguin's movement.<br>
  Right key: Begin the penguin's descent down the ramp.<br>
  A and D keys: Change teh angle at which the penguin falls.<br>
  Space key: Activate the jetpack, one of the purchaseable items in the store.<br>
<br>
Store:
As you travel further with the penguin, you gain more points. You can use those points to purchase and/or upgrade the following.<br>
  Jetpack: The jetpack applies a force in teh direction in which the penguin faces. The higher the jetpack level, the stronger the force.<br>
  Sled: The sled reduces the friction of the penguin along various surfaces. A higher level means less friction.<br>
  Glider: The glider ap[lies a force of lift which keeps the penguin in the air longer.<br>
  Ramp level: Upgrading the ramp level allows you to initially get launched down with more force.<br>

---
## Accounts

You may create your own account, however if you wish to look under the hood at some of our administrator features or you don't want to create your own please use the following accounts: <br>
```
Admin Account: 
username: caesar 
password: julius
```
```
Regular Account:
username: joe
password: steve
```
---
# From the Technical Side

## Project Environment: 
The project will be developped using the following: <br>
&nbsp;&nbsp;&nbsp;&nbsp;IDE: IntelliJ <br>
&nbsp;&nbsp;&nbsp;&nbsp;Project Archetype:  Maven <br>
&nbsp;&nbsp;&nbsp;&nbsp;Project Management Application: Jira <br>
&nbsp;&nbsp;&nbsp;&nbsp;Java Version: 21.0.2 <br>
&nbsp;&nbsp;&nbsp;&nbsp;External Libraries: FXGL, SQLite, Jackson, Lombok, Jacoco, Junit Jupiter and Slf4j

> [!NOTE]
> [Documentation Hub](https://drive.google.com/drive/folders/12dh4yEBmR-g0VRZZQVp94-PDsZ_ONl8B?usp=sharing)


--- 

## Our Class Diagram:
![Bob The Builders - Class Diagram](https://github.com/ddiliberto123/BobBuilders/assets/114122493/66c89044-7a74-493c-908a-d7d8eef00084)

--- 

## Features Implemented:
### Visual Features:
1. Speedometer and Altimeter: Monitor the penguin's speed and altityde in real-time.
2. Clouds and Snow: Encounter randomly spawned snow and clouds while in the menu or in game.
3. Changing visuals based off purchased items

### Game Mechanics:
General game mechanics, such as:
1. Point system
2. Store
3. Loading and Saving Progress
4. Admin Menu

Physics mechanics:
1. Lift
2. Drag
3. Friction
4. Gravity
5. Buyoancy

## Main in-house physics components:
1. Lift method: Computes the lift acting on the penguin based on its angle of rotation. It takes the re-established components, such as air density and the penguin's velocity to calculate and combine both lift and drag.<br>
2. Buoyancy method: Simulates buoyancy acting on the penguin as it moves horizontally. This method also includes a cap to prevent excessively large buoyancy forces that could lead to glitches. 
