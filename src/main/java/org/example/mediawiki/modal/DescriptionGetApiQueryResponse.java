package org.example.mediawiki.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionGetApiQueryResponse {

    @JsonProperty("search")
    private List<DescriptionGetApiSearchResponse> search;

    public List<DescriptionGetApiSearchResponse> getSearch() {
        return search;
    }

    public void setSearch(List<DescriptionGetApiSearchResponse> search) {

        this.search = search;
    }

}

//spring.jpa.hibernate.ddl-auto=update: Этот параметр указывает Hibernate
// (который обычно используется в Spring Boot для работы с базой данных),
// как обновлять структуру базы данных при запуске приложения. Значение update говорит Hibernate
// автоматически создавать таблицы, если они отсутствуют, и обновлять существующую структуру,
// если она изменялась.