package com.example.springboot.thriveuniversitybackend.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String[] recipient;
    private EmailTemplate msgBody;
    private String subject;


    public static class EmailTemplate{
        private String template;
        private Map<String, String> replacements;

        public EmailTemplate(String templateLocation, HashMap<String, String> replacements) {
            this.template = loadTemplate(templateLocation);
            this.replacements = replacements;
        }

        private String loadTemplate(String templateLocation) {
            Path pathToFile = Paths.get(templateLocation).toAbsolutePath();
            File file = pathToFile.toFile();
            String content = null;
            try {
                content = new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
               content = "Empty";
            }
            return content;
        }

        public String generateBody(){
            String cTemplate = template;
            for(Map.Entry<String, String> entry: replacements.entrySet()){
                cTemplate = cTemplate.replace("{{"+entry.getKey()+"}}", entry.getValue());
            }
            return cTemplate;
        }
    }

}