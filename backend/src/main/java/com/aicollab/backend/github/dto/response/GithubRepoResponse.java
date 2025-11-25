package com.aicollab.backend.github.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepoResponse {
    public long id;
    public String name;
    public String full_name;
    public Owner owner;

    public static class Owner {
        public String login;
    }
}