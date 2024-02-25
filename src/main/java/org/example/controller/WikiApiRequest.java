package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.modal.DescriptionGetApiResponse;
import org.example.modal.DescriptionGetApiSearchResponse;
import org.example.modal.Word;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class WikiApiRequest {
    private static final Logger logger = Logger.getLogger(WikiApiRequest.class.getName());

    public static List<Word> getDescriptionByTitle(String title) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Word> result = new ArrayList<>();
        String jsonResponse = "";
        try {
            String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="+title+"&srwhat=text&format=json";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response here and extract the book information
                jsonResponse = response.toString();
                DescriptionGetApiResponse responses = objectMapper.readValue(jsonResponse, DescriptionGetApiResponse.class);
                List<DescriptionGetApiSearchResponse> words = new ArrayList<>();
                for (DescriptionGetApiSearchResponse searchResponse : responses.getQuery().getSearch()) {
                    try {

                        if (searchResponse.getDescription() != null && searchResponse.getTitle() != null&& searchResponse.getId()!=null) {
                            words.add(searchResponse);
                        }
                    } catch (Exception e) {
                        logger.info(e.getMessage());
                    }
                }
                result = words.stream()
                        .map(WikiApiRequest::mapResponseToModel)
                        .collect(Collectors.toList());
            }
            else {
                if(responseCode>0){
                    logger.info(String.format("Error: %d", responseCode));
                }

            }

            conn.disconnect();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return result;
    }

    private static Word mapResponseToModel(DescriptionGetApiSearchResponse descriptionGetApiSearchResponse) {
        Word word = new Word();
        word.setId(Integer.parseInt(descriptionGetApiSearchResponse.getId()));
        word.setTitle(descriptionGetApiSearchResponse.getTitle());
        word.setDescription(descriptionGetApiSearchResponse.getDescription());
        return word;
    }

    public static Word getDescriptionByPageId(int id) throws IOException {
        String id1 = Integer.toString(id);
        String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&pageids=" + id1;
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Чтение ответа от сервера
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        // Обработка JSON-ответа
        JSONObject response = new JSONObject(content.toString());
        JSONObject pages = response.getJSONObject("query").getJSONObject("pages");
        JSONObject page = pages.getJSONObject(id1);
        String title = page.getString("title");
        String extract = page.getString("extract");

        // Обрезаем до первого вхождения тега <h2>
        int index = extract.indexOf("<h2>");
        if (index != -1) {
            extract = extract.substring(0, index);
        }

        // Удаляем все HTML-теги
        extract = extract.replaceAll("<[^>]*>", "");
        extract = extract.replaceAll("\n", "");
        Word word = new Word();
        word.setTitle(title);
        word.setDescription(extract.trim());
        return word;
    }
}
