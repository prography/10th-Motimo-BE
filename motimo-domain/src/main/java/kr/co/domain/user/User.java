package kr.co.domain.user;

public record User(Long id, String username, String password) {

    public static User of(String username, String password) {
        return new User(null, username, password);
    }
}
