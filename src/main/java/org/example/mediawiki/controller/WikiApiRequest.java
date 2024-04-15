package org.example.mediawiki.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mediawiki.modal.DescriptionGetApiResponse;
import org.example.mediawiki.modal.DescriptionGetApiSearchResponse;
import org.example.mediawiki.modal.Word;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class WikiApiRequest {
    private WikiApiRequest() {
    }

    private static final Logger LOGGER = Logger.
            getLogger(WikiApiRequest.class.getName());

    public static List<Word> getDescriptionByTitle(final String title) throws InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Word> result = new ArrayList<>();
        String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + title + "&srwhat=text&format=json";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response;
            try (client) {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                List<DescriptionGetApiSearchResponse> words = getApiSearchResponsesWords(jsonResponse, objectMapper);
                result = words.stream()
                        .map(WikiApiRequest::mapResponseToModel)
                        .toList();
            } else {
                LOGGER.info("Error with response code");
            }
        } catch (IOException e) {
            LOGGER.info("IOException");
        } catch (InterruptedException e) {
            LOGGER.info("InterruptedException");
            Thread.currentThread().interrupt();
            throw e;
        }

        return result;
    }


    private static List<DescriptionGetApiSearchResponse>
    getApiSearchResponsesWords(final String jsonResponse,
                               final ObjectMapper
                                       objectMapper) throws IOException {
        DescriptionGetApiResponse responses = objectMapper.
                readValue(jsonResponse, DescriptionGetApiResponse.class);
        List<DescriptionGetApiSearchResponse> words = new ArrayList<>();
        for (DescriptionGetApiSearchResponse searchResponse
                : responses.getQuery().getSearch()) {
            try {
                if (searchResponse.getDescription() != null
                        && searchResponse.getTitle() != null
                        && searchResponse.getId() != null) {
                    words.add(searchResponse);
                }
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
        return words;
    }

    private static Word
    mapResponseToModel(final DescriptionGetApiSearchResponse
                               descriptionGetApiSearchResponse) {
        Word word = new Word();
        word.setId(Integer.parseInt(descriptionGetApiSearchResponse.getId()));
        word.setTitle(descriptionGetApiSearchResponse.getTitle());
        word.setDescription(descriptionGetApiSearchResponse.getDescription());
        return word;
    }

    public static Word getDescriptionByPageId(final long id) throws InterruptedException {
        String id1 = Long.toString(id);
        String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&pageids=" + id1;
        URI uri = URI.create(apiUrl);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response;
            try (client) {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject pages = jsonResponse.getJSONObject("query").getJSONObject("pages");
            JSONObject page = pages.getJSONObject(id1);

            String title = page.optString("title", null);
            String extract = page.optString("extract", null);

            if (extract != null) {
                int index = extract.indexOf("<h2>");
                if (index != -1) {
                    extract = extract.substring(0, index);
                }
                extract = extract.replaceAll("<[^>]*>", "");
                extract = extract.replace("\n", "");
            }

            Word word = new Word();
            word.setTitle(title);
            word.setDescription(extract != null ? extract.trim() : null);
            return word;
        } catch (IOException e) {
            LOGGER.info("IOException");
        } catch (InterruptedException e) {
            LOGGER.info("InterruptedException");
            Thread.currentThread().interrupt();
            throw e;
        }
        return null;
    }


}
