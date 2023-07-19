package shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.client.BaseClient;
import shareit.item.dto.CommentDtoIn;
import shareit.item.dto.ItemDtoIn;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long userId, ItemDtoIn item) {
        return post("",userId,item);
    }

    public ResponseEntity<Object> getItemById(long id, long userId) {
        return  get("/"+id,userId);
    }

    public ResponseEntity<Object> getItemByUser(long userId, Integer from, Integer size) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("from", from);
        parameters.put("size", size);

        return get("",userId,parameters);
    }

    public ResponseEntity<Object> searchItems(String text, Integer from, Integer size) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("text",text);
        parameters.put("from", from);
        parameters.put("size", size);

        return  get("/search?text={text}&from={from}&size={size}",null,parameters);
    }

    public ResponseEntity<Object> addComment(Long itemId, long userId, CommentDtoIn comment) {
    return  post("/"+itemId+"/comment",userId,comment);
    }

    public ResponseEntity<Object> updateItem(long userId, long id, ItemDtoIn updatedItem) {
    return patch("/"+id,userId,updatedItem);
    }

    public ResponseEntity<Object> deleteItem(long id) {
        return delete("/"+id);
    }
}