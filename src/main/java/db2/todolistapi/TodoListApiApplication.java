package db2.todolistapi;

import db2.todolistapi.swing.frames.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class TodoListApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(TodoListApiApplication.class)
                .headless(false)
                .run(args);

        // Mostra a janela principal
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = context.getBean(MainFrame.class);
            mainFrame.setVisible(true);
        });
    }

}
