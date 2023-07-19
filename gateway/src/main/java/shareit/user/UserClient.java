package shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.client.BaseClient;
import shareit.user.dto.UserDto;


@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public ResponseEntity<Object> getAllUsers() {
        int k =1;
        return get("");
    }

    public ResponseEntity<Object> createUser(UserDto userDTO) {
        int k =1;
        return post("",userDTO);
    }

    public ResponseEntity<Object> getUserById(long id) {
        return get("/"+id+"");
    }

    public ResponseEntity<Object> updateUser(long id, UserDto userDto) {
        return patch("/"+id,userDto);
    }

    public ResponseEntity<Object> deleteUser(long id) {
        return delete("/"+id);
    }
}
