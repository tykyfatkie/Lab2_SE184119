=================================================
  LAB211 - J1.L.P0036 - Football Club & Player Management
  IDE: Apache NetBeans 13  |  JDK: 8+
=================================================

HOW TO RUN IN NETBEANS:
  1. Open NetBeans 13
  2. File > New Project > Java > Java Application > Next
  3. Project Name: FootballClub
  4. Uncheck "Create Main Class" > Finish
  5. Copy all .java files into the project src folder
     (maintain package folders: model/, manager/, utils/, ui/)
  6. Put clubs.txt and players.txt in the PROJECT ROOT folder
  7. Right-click project > Properties > Run > Main Class: Main
  8. Run (F6)

HOW TO RUN VIA COMMAND LINE:
  javac -encoding UTF-8 -d out $(find src -name "*.java")
  java -Dfile.encoding=UTF-8 -cp out Main

PACKAGE STRUCTURE:
  Main.java          - Entry point
  model/
    Entity.java      - Abstract base class (OOP: Abstraction + Inheritance)
    Club.java        - Club entity
    Player.java      - Player entity
  manager/
    ClubsManager.java   - Club CRUD logic (implements Manageable, Saveable)
    PlayersManager.java - Player CRUD logic (implements Manageable, Saveable)
  utils/
    Manageable.java     - Generic CRUD interface (OOP: Interface + Polymorphism)
    Saveable.java       - File I/O interface
    ValidationUtils.java - All input validation
    FileUtils.java       - File read/write operations
  ui/
    Menu.java           - Console menu (14 functions)
