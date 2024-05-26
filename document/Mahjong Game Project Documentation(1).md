# Mahjong Game Project Documentation

<center>
Group 7

Members: Le Liu: 22207256, Ziheng Wang: 22207280,

Sicheng Yi: 22207275, Kuize Lu: 22207294
</center>


## Introduction

Mahjong is a board game originating from China. The game is usually played by four people. The rules of Mahjong vary greatly from place to place, but the basic goal is to create certain combinations of cards through a series of substitution and trade-off rules, and to prevent opponents from achieving the same goal. 

Mahjong has a variety of combinations. In addition to some luck, it also focuses on skills and the use of strategies between drawing and discarding tiles. Compared with other games, Mahjong not only requires more memory skills and complex probability calculations, but also requires guessing the possible card types in the hands of the other three players to have a chance to win the game. 

This document details the specific requirements, UML diagram, specific project plan and team member division of labor of the project, aiming to develop a comprehensive mahjong game application. This document will serve as the basis for development and will be used for guidance and evaluation by team members and instructors.

## Project Overview

The Mahjong game project aims to provide an authentic and enjoyable Mahjong game experience. The game will contain a standard set of 136 tiles, with three main categories: Suits and Honors tiles. Each category plays a vital role in gameplay, affecting strategy and scoring.

**Suits:** divided into three types: bamboo, character, and circle, each from one to nine.

**Honors:** This group includes winds (east, south, west, north) and dragons (red, green, white).

The game will continue to adhere to traditional Mahjong rules and strategies, such as the formation of "combinations" (specific combinations of tiles such as "Pung", "Chow" and "Kong"), which are crucial to winning. The interface will be designed to be user-friendly, with easy-to-use controls and a visually pleasing layout that enhances the overall player experience.

Detailed Mahjong rules are provided at the end of the document..

## Requirements Analysis

### User Requirements

1. Users has the ability to start and end a game at any point during gameplay.
2. The game provides a comprehensive guide on the rules of Mahjong, ensuring all rules are easy to understand and apply.
3. Users receives contextual tips during gameplay to aid in decision-making, such as recommendations to 'Pung' or 'Kong'.
4. The game interface should be straightforward and intuitive, facilitating easy actions like tile drawing and discarding.
5. Users expect a seamless gameplay experience with fast response times for actions like drawing and discarding tiles, and forming combinations like 'Chow', 'Pung', and 'Kong'.

### System Requirements

#### Functional Requirements

1. **Game Rules Engine:**
   - The game simulate all essential Mahjong rules, manage tile dealing, and handle actions like 'Chow', 'Pung', 'Kong', and determining winning conditions.
2. **User Interaction:**
   - The system features a user-friendly interface.
   - Players should be able to manipulate Mahjong tiles using mouse inputs.
   - In-game tutorials and hints shall be available to assist users in understanding the game rules and improving their gameplay.
3. **Scoring and Winning Calculation:**
   - Implement algorithms to calculate scores and determine winning hands based on the combinations of tiles formed by players, following the rules of Mahjong.

#### Non-Functional Requirements:

##### Product Requirements

1. **Reliability:**
   - The system should be highly reliable, with minimal downtime and robust error handling mechanisms to prevent crashes or disruptions during gameplay.
2. **Security:**
   - Implement robust security measures to protect player data, prevent cheating, and ensure fair gameplay, maintaining the integrity and trustworthiness of the game system.
3. **Performance:**
   - The system should respond to user interactions promptly, with minimal delay between actions.
4. **Development Requirements:**
    - Development conforms to coding standards using Java version: JDK21
    - Code follows to Java best practices and be managed through GitHub for version control, incorporating thorough documentation and code review processes.

## UML

**Use Case Diagram**

![50268faede8b505595a3c20eb906574](https://mrle-1316607909.cos.ap-hongkong.myqcloud.com/50268faede8b505595a3c20eb906574.png)

**Sequence Diagram**
![50268faede8b505595a3c20eb906574](https://mrle-1316607909.cos.ap-hongkong.myqcloud.com/5e913206cc0334901ff93141189ac5c.png)

**Class Diagram**
![1](http://www.plantuml.com/plantuml/png/bLRDRjj64BxpARRgHKciGmyzr2YC16cG570J0pOQ83-7CJaMjoPtrTsXKxde4_KHUikJbSCTqXnveKZsGTJEppVppRT3FOy4WRgsARwZ6biqZLtNj0wmBSXl37bpxObyQoeRi2JhNH5lhTj0WBRtTKZR3HeVgFP5KJOGetcnsJIs1FOrVnV6F3CjMBUyf63T-kFd9wVcpjkg-4VSNzPOtbxP1WVdbGc-8-lmFMoNvpRIpsmwE-dBy0s2wwFbQSXSWhj0jrQU_A7SLgRgxBrzqytSTlBSM5VnkLJPM_dR2F4QzaEmcurlk_OQmrNFnp9ZPuGBi47b-GsRYWEcZ0z6IVgjeY_npmvTYIfjDXatyWhew3xuOUOpZkqrjEF4QeZlhNCODEbb5m8wkcXWowRqELWsmsEK2fFXRB1G3tX9KDxo0EHnCCGI4i9bUXXg7RJBXIn5LAa3Pyb2O-zHcuIn3tN7utmog-YPLFHkWs7GRLmcMs9e4jyy5LHLyb401BTurGKdyRb8mVh5nX929OuxGuYr__AYQHWygloLZHJiTKV8DcLg_HrU-RVu5qqvzKLx0wwIYH4dLWBXqvRLA1Oj7JyIizIAdU6Bl7BIhrAhGyNJL4-CN3Nj2zNefxElaOIDUN5g1up8EXiNdCqOa1bB9XMfRdtEn0lhLvRyKbkuI8RB5YfJvI4y7clqOMnF3qqqAcEpCObNtxF6fsKx2fh78-wvQrc3gqvP31EnvvA0mUSfk30akIyP8O_bA1hszSbWRE0p_ZnQ7Ujn9Ee-OUPmx7stjYh05mtrmBXGFdiNaUo_j8qcOk-WwVepwoWjkXe2bCH4fwg5_NEm7-mZXVpEmLmMXml7WsUEudwxGnENxwx_m98Ug-tbYCpv19jDvDCNVJoMb97js1wRnkT9WnDLmefCBBCgSgjb__MLbMcR2LekPqIrsHLJUlU9uYI_qi2hTaDRtUwgT-vkRdOJsFlPJ5QpTuXAczdVcKKbfap3BB9Y_ll8h3v-_-DF_7zIGQon-hmwC-dTAd-dPl7rtp8W47xzRz4V5DCF9ZPduyMOZpV5F7q-0LzEiHyO8Nig0wc25pyipDE713sLrd7yGduIV79F5vBhS73Lxs5mD4hwa-jT9e4V3YVkkxqguhB1QdgCno47rPl8KRr-6qwr9iJkX-Bt0xEYH22XXsf6ix8lN1Lw19hgIsAFONsAFv9r7Nly1IoUewkwjlaV)

## Project Plan

### Overview:

**Step 0: Requirement and Documentation (Week 3 - 6)**


**Step 1: Command Line Mahjong Game (Week 7 - 10)** 

- Develop a Mahjong game playable in the command line interface.
- Implement all essential game functionalities, including tile management, player actions (drawing, discarding), and basic rule enforcement (win conditions, scoring).
- Ensure the game is fully functional and playable solely through command line inputs.
- Conduct thorough testing to identify and resolve any bugs or issues.

**Step 2: UI Interface Development (Week 11 - 12)**

- Design and develop a graphical user interface (GUI) for the Mahjong game.
- Create intuitive UI elements for player interaction, including menus, game board, and player controls.
- Integrate visual and audio enhancements to improve user experience.
- Implement functionality to seamlessly transition between game states and handle user inputs.
- Conduct extensive testing to ensure the GUI version of the game is stable and user-friendly.

### Detailed


**Week 3 - 6: Requirement and Documentation** 

- Gather information and system requirements through structured discussions and brainstorming sessions.
- Create and review initial project documentation, including user stories and functional specifications.
- Finalize project scope and initial project plan.

**Week 7 - 8: Foundation Establishment**

- Define fundamental game rules with precision, ensuring clarity and consistency.
- Finalize project documentation, including UML diagrams and member contribution delineation.
- Implement Mahjong tile classes and logic, including deck management, shuffling, and dealing functionalities.
- Enable core player interactions such as drawing and discarding tiles to establish basic gameplay mechanics.

**Week 9-10: Rule Logic Development**

- Engineer the intricate game rule logic, encompassing win conditions like "hu" (winning hand) and overarching victory criteria.
- Design the overall logic of how the game works.
- **First Test.**

**Week 11-12: UI Design and Enhancement**

- Design UI elements to enhance user experience, including main menu, game interface, and settings.
- Optimize game logic for seamless gameplay experience.

**Week 13-14: User Testing and Iteration**

- **Second Test**. Conduct user testing sessions to gather feedback for iterative improvements.

**Week 15: Final Testing and Release Preparation**

- Prepare release version, including documentation and resource compilation.
- Finalize project submission.

# Task allocation

### Ziheng Wang

Responsible for defining game rules, ensuring their clarity and consistency, 
and designing overall game logic. He is also responsible for designing UI elements to enhance the user experience, 
such as the main menu and game interface, and preparing the release version in the final stages, 
including documentation and resource compilation.

### Le Liu

Responsible for finalizing project documentation, including UML diagrams and division of member contributions. 
He will implement complex game rules logic, including victory conditions and overall victory criteria, 
and collect feedback for iterative improvements during the user testing phase. 
Finally, he ensures the final submission of the project.

### Kuize Lu

Responsible for implementing the mahjong tile class and logic, including deck management, 
shuffling and dealing functions, and conducting the first test in the early stages. 
He will optimize game logic to ensure a smooth gaming experience and participate in iterative 
improvements during the second testing phase.

### Sicheng Yi

Responsible for enabling core player interactions such as drawing and discarding cards to establish basic game mechanics. 
He will be involved in designing the overall logic of the game and implementing UI features to 
ensure seamless game state transitions and handling of user input. During the user testing phase, 
he actively addresses feedback and makes necessary adjustments.





# ***Mahjong Rules***

## **Composition of Mahjong Tiles**

1. Character Tiles: (Including Wind Tiles and Dragon Tiles) <br>
   Wind Tiles: East, South, West, North (4 each, total 16 tiles)<br>
   Dragon Tiles: Red, Green, White (4 each, total 12 tiles)

2. Number Tiles: (Including Characters, Circles, Bamboo, 36 each, total 108 tiles)<br>

3. Joker Tiles:<br>
   ① Joker tiles are special tiles in Mahjong, typically 4 per set of tiles.<br>
   ② Joker tiles do not have a fixed suit or number; they can replace any other tile in the game to form a winning hand.<br>
   ③ Joker tiles are randomly assigned during the game and require players to specify combinations for them to be used.<br>
   ④ Joker tiles can be discarded following their own tile type when discarding, enabling actions like eat, peng, and gang.<br>
   ⑤ Joker tiles cannot be used as Wind Tiles (East, South, West, North) or Dragon Tiles (Red, Green, White).

## Introduction to Mahjong Operations

**1. Basic Overview:**<br>

① Initial Hand Tiles: 13 tiles per player. <br>
② Winning Conditions: Achieved through combinations such as eat, peng, and gang to form the required hand according to specific tile patterns. (If no player wins after the tile stack is depleted, a draw is settled.)<br>
③ Turn Sequence: Drawing, discarding, "Pung", "Chow" and "Kong" are performed in a counterclockwise direction based on seating.<br>
④ Tile Combinations:<br>
Pair: Two identical tiles (e.g., 2 Bamboo, 2 East Wind, 2 White Dragon). <br>
Sequence: A group of three consecutive tiles belonging to the same suit (either Bamboo, Character, or Circle), arranged in numerical order.<br>
Triplet: A group of three identical tiles (e.g., 9 Bamboo, 9 Bamboo, 9 Bamboo).

**2. Mahjong Operations:**<br>
① Pung: When you have a pair in your hand and another player discards the third identical tile, you can pung to form a set of three identical tiles, then discard one unnecessary tile from your hand.<br>
② Kong: When you have a triplet in your hand and you draw the fourth identical tile yourself, you can declare a kang. This can be either a concealed kang (scored as concealed) or an exposed kang if the fourth tile is drawn from the wall or claimed from another player's discard. If you previously punged to form a set of three identical tiles and then draw the fourth, this is called a supplement kang. After declaring a kang, you draw an extra tile from the wall. When a tile can be used for both kang and winning, winning takes priority.<br>
③ chow: When you have two tiles forming a potential sequence in your hand and the preceding player discards the third tile to complete the sequence, you can chow to form a sequence of three tiles. If a tile can be claimed for both chow and pung (or kang), pung (or kang) takes priority.<br>
④ Priority of Claiming: When two players declare mahjong simultaneously, the priority is: <br>
    hu card > pung, kang > chow
   If two players claim mahjong on the same discarded tile, the player sitting to the right of the discarder has the priority to claim mahjong.<br>
⑤ Exposed Tiles: After any chow, pung, or kang action, players must openly display the tiles involved and place them in their tile sets.<br>
⑥Skip: When either player performs a pung or a kang, the next player to perform this turn will be that player, and if that player is not the next player to the previous player, the players who had not started in the middle is skipped.<br>

**3. Winning Conditions:Hu card**<br>
Explanation:AAA = triplet, AAAA = kang, ABC = sequence, DD = pair, Number = single tile <br>
Basic Formula: nAAA + mABC + p*DD = 14

① Basic Winning Patterns:<br>
   _(1) DD, ABC, ABC, ABC, ABC_ <br>
   _(2) DD, ABC, ABC, ABC, AAA (or AAAA)_<br>
   _(3) DD, ABC, ABC, AAA, AAA_<br>
   _(4) DD, ABC, AAA, AAA, AAA_<br>
   _(5) DD, AAA, AAA, AAA, AAA_<br>

② Special Winning Patterns:<br>
   (1) Seven Pairs: Consists of seven pairs of identical tiles, totaling 14 tiles.<br>
       Example: DD, DD, DD, DD, DD, DD, DD
       
   (2) Thirteen Orphans (Thirteen Unique Wonders): Must include all one and nine tiles, along with the East, South, West, North, Red, Green, and White Dragon tiles, plus any additional pair of identical tiles.<br>
       Example: 1 Bamboo, 9 Bamboo, 1 Circle, 9 Circle, 1 Character, 9 Character, East, South, West, North, Red, Green, White + any identical pair.
       
   (3) Big Three Dragons: Consists of three sets of triplets or gangs of dragon tiles (Red, Green, White).   
       Example: AAA, AAA, AAA, DD, DD, DD
       
   (4) Big Four Winds: Contains four sets of triplets or gangs of wind tiles (East, South, West, North).       
       Example: East, East, East, South, South, South, West, West, West, North, North, North, DD
       
   (5) All Honors: Consists entirely of honor tiles (Wind and Dragon tiles).
       Example: East, East, East, South, South, South, West, West, West, North, North, North, Red, Red