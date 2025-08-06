# QueryProcessor AI

An intelligent log processing system **built from scratch in Java** that converts natural language queries to SQL using Google Gemini AI and processes log files with traditional SQL operations.

##  Key Features

- ** AI-Powered Natural Language Processing**: Convert plain English to SQL queries using Google Gemini AI
- ** Multi-Query Support**: Natural language, SQL SELECT, and simple text search
- ** Cross-Platform Compatible**: Runs on Windows, Linux, macOS - works with serialized/deserialized log data
- ** Smart Rate Limiting**: Built-in API throttling and retry mechanisms
- ** Interactive CLI**: Colorful terminal interface with real-time feedback
- ** Flexible Log Processing**: Handles various log formats with timestamp, severity, and content filtering
- **️ Built from Scratch**: Custom Java implementation with no external frameworks

##  Installation

### Prerequisites
- Java 24 or later
- Maven 3.6+
- Google Gemini API Key

### Setup

1. **Clone and build**:
   ```bash
   git clone <repository-url>
   cd queryProcessorAI
   mvn clean compile
   ```

2. **Configure API key** (choose one):
   ```bash
   # Option A: Environment variable (recommended)
   export GEMINI_API_KEY="your_api_key_here"
   
   # Option B: Create api.properties file
   cp src/main/resources/api.properties.example src/main/resources/api.properties
   # Edit api.properties and add your key
   ```

3. **Run**:
   ```bash
   mvn exec:java -Dexec.mainClass="logger.App"
   ```

Get your Gemini API key from [Google AI Studio](https://aistudio.google.com/app/apikey).

## 💡 Usage

### Interactive Commands

| Command | Description | Example |
|---------|-------------|---------|
| `nlp` | Natural language query | "Show me all error logs from today" |
| `find` | Text search | Search for "exception" |
| `select` | SQL query | `SELECT * WHERE severity='ERROR'` |
| Direct SQL | Type SQL directly | `SELECT * FROM logs WHERE data LIKE '%timeout%'` |

### Example Session

```bash
Log file path: /path/to/your/logfile.log
✓ Using log file: /path/to/your/logfile.log

[LogQuery] > nlp
Your query: Show me all error logs from yesterday
AI-generated SQL: SELECT * FROM logs WHERE severity = 'ERROR' AND timestamp >= '2024-01-05'
✓ Query execution completed

[LogQuery] > find
Search term: database connection
# Shows all log entries containing "database connection"

[LogQuery] > SELECT * FROM logs WHERE data LIKE '%timeout%'
# Direct SQL execution
```

## 🔧 How It Works

1. **Universal Log Processing**: Reads serialized log data, making it platform-independent
2. **AI Translation**: Gemini AI converts natural language to SQL
3. **Smart Parsing**: Handles timestamps, severity levels, and content filtering
4. **Cross-Platform**: Java-based solution works on any OS with JVM
5. **Custom Implementation**: Built entirely from scratch using core Java and minimal dependencies

## 📄 License

MIT License - see LICENSE file for details.

## 🚨 Troubleshooting

- **No API key**: Set `GEMINI_API_KEY` environment variable
- **Rate limits**: App automatically retries with backoff
- **File not found**: Ensure log file path is correct and readable
