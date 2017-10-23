#!/bin/bash

# In case you use the provided ParseJSON.java code for preprocessing the wikipedia dataset, 
# uncomment the following two commands to compile and execute your modified code in this script.
#

javac ParseJSON.java
java ParseJSON

# TASK 2A:
# Create and index the documents using the default standard analyzer

curl -XPUT 'localhost:9200/task2a?pretty' -H 'Content-Type: application/json' -d'
{
  "settings": {
    "number_of_shards": 5,
    "number_of_replicas": 2
  },
  "mappings": {
    "wikipage": {
      "_all" : {"type" : "string", "analyzer" : "standard"},
      "properties": {
        "abstract": {
          "type": "string",
          "analyzer": "standard"
        },
        "sections": {
          "type": "string",
          "analyzer": "standard"
        },
        "title": {
          "type": "string",
          "analyzer": "standard"
        },
        "url": {
          "type": "string",
          "analyzer": "standard"
        }
      }
    }
  }
}
'
curl -XPOST  localhost:9200/task2a/_bulk?pretty=true --data-binary @data/out.txt

curl -s -XPOST 'localhost:9200/_refresh?pretty'


# TASK 2B:
# Create and index with a whitespace analyzer

curl -XPUT 'localhost:9200/task2b?pretty' -H 'Content-Type: application/json' -d'
{
  "settings": {
    "number_of_shards": 5,
    "number_of_replicas": 2
  },
  "mappings": {
    "wikipage": {
      "_all" : {"type" : "string", "analyzer" : "whitespace"},
      "properties": {
        "abstract": {
          "type": "string",
          "analyzer": "whitespace"
        },
        "sections": {
          "type": "string",
          "analyzer": "whitespace"
        },
        "title": {
          "type": "string",
          "analyzer": "whitespace"
        },
        "url": {
          "type": "string",
          "analyzer": "whitespace"
        }
      }
    }
  }
}
'

curl -XPOST  localhost:9200/task2b/_bulk?pretty=true --data-binary @data/out.txt

curl -s -XPOST 'localhost:9200/_refresh?pretty'


# TASK 2C:
# Create and index with a custom analyzer as specified in Task 2C
curl -XPUT 'localhost:9200/task2c?pretty' -H 'Content-Type: application/json' -d'
{
  "settings": {
    "analysis": {
      "char_filter": {
        "my_char_filter": {
          "type": "html_strip"
        }
      },
      "tokenizer":{
        "my_tokenizer":{
          "type": "standard"
        }
      },
      "filter": {
        "my_ascii_folding": {"type": "asciifolding"},
        "my_lower_case": {"type": "lowercase"},
        "my_stop_token": {"type": "stop", "stopwords": "_english_"},
        "my_snowball_token": {"type": "snowball"}
      },
      "analyzer": {
        "my_analyzer": {
          "type": "custom",
          "char_filter": "my_char_filter",
          "tokenizer": "standard",
          "filter": ["my_ascii_folding", "my_lower_case", "my_stop_token", "my_snowball_token"]
        }
      }
    },
    "number_of_shards": 5,
    "number_of_replicas": 2
  },
  "mappings": {
    "wikipage": {
      "_all": {"type": "string", "analyzer": "my_analyzer"},
      "properties": {
        "abstract": {
          "type": "string",
          "analyzer": "my_analyzer"
        },
        "sections": {
          "type": "string",
          "analyzer": "my_analyzer"
        },
        "title": {
          "type": "string",
          "analyzer": "my_analyzer"
        },
        "url": {
          "type": "string",
          "analyzer": "my_analyzer"
        }
      }
    }
  }
}
'

curl -XPOST  localhost:9200/task2c/_bulk?pretty=true --data-binary @data/out.txt

curl -s -XPOST 'localhost:9200/_refresh?pretty'



