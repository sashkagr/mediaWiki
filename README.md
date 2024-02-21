# Data from Wikipedia

***
The service should accept some term/word and return a text search result from Wikipedia corresponding to the word
* results must be requested from the Wikipedia API
***
At the moment, a GET endpoint has been added that accepts input parameters (word/term) as queryParams in the URL and returns the result (definition) as JSON.
* The path to this project looks like this:
```
http://localhost:8080/definition?name=(word/term you want to search for)
```
