package org.example.mediaWiki.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionGetApiSearchResponse {

       @JsonProperty("pageid")
       private String id;
       public String  getId() {
        return id;
       }
    public void setId(String id) {
        this.id = id;
    }

        @JsonProperty("title")
        private String title;
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        @JsonProperty("snippet")
        private String description;
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }



    }
