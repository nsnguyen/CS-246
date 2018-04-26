#!/bin/bash

# In case you use the provided ParseJSON.java code for preprocessing the wikipedia dataset, 
# uncomment the following two commands to compile and execute your modified code in this script.
#
javac ParseJSON.java
java ParseJSON

# TASK 1A:
# Create index "task1a" with "wikipage" type using BM25Similarity 


curl -XPOST localhost:9200/task1a/_bulk?pretty=true --data-binary @data/out.txt


# TASK 1B:
# Create index "task1b" with "wikipage" type using ClassicSimilarity (Lucene's version of TFIDF implementation)

curl -XPUT 'localhost:9200/task1b?pretty' -H 'Content-Type: application/json' -d'
{
  "settings": {
    "index": {
      "similarity": {
        "default": {
          "type": "classic"
        }
      }
    }
  }
}
'

curl -XPOST localhost:9200/task1b/_bulk?pretty=true --data-binary @data/out.txt




# TASK 2:
# Create index "task2" with "wikipage" type using BM25Similarity with the best k1 and b values that you found


curl -XPUT 'localhost:9200/task2?pretty' -H 'Content-Type: application/json' -d'
{
  "settings": {
    "index": {
      "similarity": {
        "default": {
          "type": "BM25",
          "k1" : 0.80,
          "b" : 0.50
        }
      }
    }
  }
}
'

curl -XPOST localhost:9200/task2/_bulk?pretty=true --data-binary @data/out.txt



# Task 3:
# Create index "task3" with "wikipage"

curl -XPUT 'localhost:9200/task3b?pretty' -H 'Content-Type: application/json' -d'
{
  "settings": {
    "index": {
      "similarity": {
        "default": {
          "type": "cs246-similarity"
        }
      }
    }
  },	
  "mappings": {
    "wikipage": {
      "properties": {
        "abstract": {
          "type": "text"
        },
        "sections": {
          "type": "text"
        },
        "title": {
          "type": "text"
        },
        "clicks" : {
            "type": "long",
            "index": "not_analyzed",
            "doc_values": true
          },
        "url": {
          "type": "text"
        }
      }
    }
  }
}
'

curl -XPOST localhost:9200/task3b/_bulk?pretty=true --data-binary @data/out.txt



