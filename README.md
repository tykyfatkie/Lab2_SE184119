# Football Club & Player Management
## LAB211 – J1.L.P0036

### How to Run
```bash
# 1. Compile
javac -d out $(find src -name "*.java")

# 2. Run (from project root, so data/ path resolves correctly)
java -cp out football.Main
```

### Project Structure
```
FootballClub/
├── data/
│   ├── clubs.txt       ← club data file
│   └── players.txt     ← player data file
└── src/main/java/football/
    ├── Main.java               ← entry point
    ├── Menu.java               ← UI loop (14 menu items)
    ├── model/
    │   ├── Club.java           ← Club entity
    │   └── Player.java         ← Player entity
    ├── manager/
    │   ├── ClubsManager.java   ← Club CRUD operations
    │   └── PlayersManager.java ← Player CRUD operations
    └── utils/
        ├── ValidationUtils.java ← all validation helpers
        └── FileUtils.java       ← file read/write
```

### Menu Options
| # | Feature |
|---|---------|
| 1 | List all clubs |
| 2 | Add a new club |
| 3 | Search club by ID |
| 4 | Update club by ID |
| 5 | List clubs with budget ≤ value |
| 6 | List all players sorted by club name then shirt# |
| 7 | Search players by partial name |
| 8 | Add a new player |
| 9 | Remove a player |
|10 | Update a player |
|11 | List players by position |
|12 | Save data to files |
|13 | Load data from files |
|14 | Quit (auto-saves if changes detected) |
