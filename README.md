# Stock App 📈 (Java + SQLite + Docker)

A simple Java application that simulates stock price tracking, persists data into an SQLite database, and runs inside Docker.  
The app periodically generates Dow Jones prices and stores them for later analysis.

---

##  Features
- Mock stock price generator (`MockStockGenerator`)
- Can use Yahoo Stock Finance API although this may take longer
- Automatic persistence in SQLite (`DatabaseManager`)
- Keeps last 20 entries in memory queue
- Bulk insert simulation (1000+ records)
- Unit tests with JUnit 5
- Packaged with Gradle & Docker

---

##  Requirements
- [Java 24+](https://adoptium.net/) (via Gradle toolchain, no manual install needed)
- [Gradle](https://gradle.org/) (wrapper included)
- [Docker Desktop](https://www.docker.com/products/docker-desktop)

---

## Running Locally

### 1. Clone the repository
```bash
git clone https://github.com/<your-username>/stock-app.git
cd stock-app

### 2. Build the project
./gradlew build

### 3. Run the app
./gradlew run

This will:
- Start generating mock stock prices every 5 seconds
- Insert them into stocks.db

Running with Docker
### 1. Build Docker image
docker build -t stock-app .

### 2. Run container
docker run --rm stock-app

Inside the container (or locally):

### 3. Retrieve DB entries
sqlite3 stocks.db
.tables
SELECT COUNT(*) FROM stock_data;
SELECT * FROM stock_data LIMIT 5;


Example output:

id | timestamp           | price
---+---------------------+--------
1  | 2025-09-22 01:23:42 | 34016.46
2  | 2025-09-22 01:23:47 | 34058.25
3  | 2025-09-22 01:24:52 | 34082.10

### Notes:
For real stock prices, replace MockStockGenerator with the YahooFinance API
e.g. 
Stock dowJones = YahooFinance.get("^DJI");
BigDecimal price = dowJones.getQuote().getPrice();

To change the frequency of the stock price insertions into the DB change the third argument in this line of code in App.java:

scheduler.scheduleAtFixedRate(fetchTask, 0, 5, TimeUnit.SECONDS);
