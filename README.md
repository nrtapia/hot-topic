# Exercise

Implement a hot topic analysis for RSS feeds.

## Specification
Your application should expose two HTTP endpoints:

### API Definition: 

```
/analyse/new
```

```
curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'[
  "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss",
 "https://news.yahoo.com/rss"
]' \
 'http://localhost:8080/analyse/new'
```

### API Input:

This API endpoint should take at least two RSS URLs as a parameter (more are possible) e.g.:

https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss

### API Response:

For each request executed against the API endpoint you should return an unique identifier, which will be the input for the second API endpoint.

```
{
  "id": "f0b6f8a6-5a8d-4fdc-b149-4554314d5772"
}
```

### Workflow:

When the the API is being called, your code should do a HTTP request to fetch the RSS feeds.
Your code should then analyse the entries in this feed and find potential hot topics --> are there any overlaps between the news.

### Example:

RSS Feed one contains following news:
To Democrats, Donald Trump Is No Longer a Laughing Matter
Burundi military sites attacked, 12 insurgents killed
San Bernardino divers return to lake seeking electronic evidence

RSS Feed two contains following news:
Attacks on Military Camps in Burundi Kill Eight
Saudi Women to Vote for First Time
Platini Dealt Further Blow in FIFA Presidency Bid

Your analysis should return that there are news related to Burundi in both feeds.
The analysed data should be stored within a data store and referenced by an unique identifier (see API response).

### API Definition: 

```
/frequency/{id}
```

```
curl -i -X GET \
 'http://localhost:8080/frequency/f0b6f8a6-5a8d-4fdc-b149-4554314d5772'
```

### API Input:

This API endpoint takes an id as input

### API Output:

Returns the three elements with the most matches, additinally the orignal news header and the link to the whole news text should be displayed.

```
{
    "hotTopics": [
        {
            "topic": "coronavirus",
            "count": 27
        },
        {
            "topic": "trump",
            "count": 8
        },
        {
            "topic": "virus",
            "count": 5
        }
    ],
    "news": [
        {
            "title": "House Conservatives Who Attacked Dr. Fauci Should Know Better",
            "link": "https://news.yahoo.com/house-conservatives-attacked-dr-fauci-103021465.html"
        },
        {
            "title": "Ivanka Trump skirted coronavirus guidelines to travel to N.J. for Passover",
            "link": "https://news.yahoo.com/ivanka-trump-skirted-coronavirus-guidelines-112635726.html"
        },
        {
            "title": "&#39;I had to go through it&#39;: Trump says he was a &#39;victim&#39; of the COVID-19 test and referred to it as &#39;an operation&#39;",
            "link": "https://news.yahoo.com/had-trump-claims-victim-covid-211624561.html"
        }
    ]
}
```

### Workflow:

When this API is being called, you will read the analysis data stored in the database by using the given id parameter
Return the top three results as a json object ordered by their frequency of occurrence

## Additional Information
You should use following frameworks for your work.

Spring JPA
H2 database running in memory (data will not be persistent across application restarts)
You are free to add / change any libraries which you might need to solve this exercise, the only requirement is that we do not have to setup / install any external software to run this application.

Running the exercise with maven

```mvn spring-boot:run```