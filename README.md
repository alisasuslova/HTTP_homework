# Запрос на получение списка фактов о кошках

## Входные данные:
По адресу https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats находится список фактов о кошках. 
Формат каждой записи следующий:
```json
{
  "id": "5b4910ae0508220014ccfe91",
  "text": "Кошки могуть создавать более 100 разных звуков, тогда как собаки только около 10.",
  "type": "cat",
  "user": "Alex Petrov",
  "upvotes": null
},
```
```text
id - уникальный идентификатор записи
text - сообщение
type - тип животного
user - имя пользователя
upvotes - голоса
```

## Выходные данные:
- Сделать запрос к этому ресурсу и отфильтровать факты, за которые никто не проголосовал (поле upvotes), а все остальные вывести на экран: 

```text
{
"id": "5b4910ae0508220014ccfe90",
"text": "Кошки могут слышать ультразвук и коммуницировать с дельфинами.",
"type": "cat",
"user": "Alex Petrov",
"upvotes": 12
},
{
"id": "5b4910ae0508220014ccfe93",
"text": "Самой долгоживущей кошкой из всех когда-либо зарегистрированных считается Крим Пафф, прожившая 38 лет и 3 дня.",
"type": "cat",
"user": "Maxim Semenov",
"upvotes": 2
},
{
"id": "5b4910ae0508220014ccfe94",
"text": "Обоняние у кошек примерно в 14 раз сильнее человеческого, что позволяет им чувствовать запахи, о которых человек даже не подозревает.",
"type": "cat",
"user": "Elena Ivanova",
"upvotes": 9
},
{
"id": "5b4910ae0508220014ccfe95",
"text": "В то время как в большинстве стран чёрная кошка считается символом несчастья, в Великобритании и Австралии, они, наоборот, рассматриваются как животные, приносящие удачу.",
"type": "cat",
"user": "Elena Ivanova",
"upvotes": 3
}
```

## Реализация:
1. Создала проект `maven` и добавила в pom.xml библиотеку apache httpclient:
```text
<dependency>
   <groupId>org.apache.httpcomponents</groupId>
   <artifactId>httpclient</artifactId>
   <version>4.5.13</version>
</dependency>
```
2. В Main создала метод  `CloseableHttpClient` с помощью builder:
```text
CloseableHttpClient httpClient = HttpClientBuilder.create()
    .setDefaultRequestConfig(RequestConfig.custom()
        .setConnectTimeout(5000)    
        .setSocketTimeout(30000)    
        .setRedirectsEnabled(false) 
        .build())
    .build();
```
3. Добавила объект запроса: 
```text
HttpGet request = new HttpGet(SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request);
```
4. В pom.xml подключила библиотеку для работы с json:
```text
<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.13.1</version>
</dependency>
```
5. Создала класс `Cat.java`, в который будет преобразовываться json ответ от сервера:
```text
public class Cat {
    private final String id;
    private final String text;
    private final String type;
    private final String user;
    private final String upvotes;

    public Cat(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text,
            @JsonProperty("type") String type,
            @JsonProperty("user") String user,
            @JsonProperty("upvotes") String upvotes
    ) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.user = user;
        this.upvotes = upvotes;
    }
}
```

6. Преобразуовала json в список java объектов:
```text
 Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
```

7. Отфильтровала список с помощью метода filter и вывела результаты на экран:
```text
List<Cat> posts = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<>() {
                }
        );
        posts.stream()
                .filter(value -> value.getUpvotes() != null)
                .forEach(System.out::println);
```