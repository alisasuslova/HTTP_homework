import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static final String SERVICE_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);

        Arrays.stream(response.getAllHeaders()).forEach(System.out::println); //создаем массив заголовков getAllHeaders() и выводим их в консоль
        System.out.println("\nBody:\n"); //потом убрать


        //вывод 2й вариант:
        List<Cat> posts = mapper.readValue(
                response.getEntity().getContent(),  //принимает поток
                new TypeReference<>() { // и тип данных в который мы хотим перевести, <Post>!
                }
        );
        // posts.forEach(System.out::println); //по одному выводим каждый объект

        posts.stream()
                .filter(value -> value.getUpvotes() != null)
                .forEach(System.out::println);


        response.close(); //вручную закрываем, т.к. нет  try-catch
        httpClient.close(); //вручную закрываем, т.к. нет  try-catch


    }
}
