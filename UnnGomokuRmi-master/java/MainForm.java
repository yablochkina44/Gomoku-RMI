import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    private static final int DEFAULT_WIDTH = Field.DEFAULT_COUNT * GameField.CELL_SIZE + 7;
    private static final int DEFAULT_HEIGHT = DEFAULT_WIDTH + 23;
    private static final int DEFAULT_MARGIN = 50;
    // Здесь определены константы для ширины, высоты и отступов окна.
    // Размеры зависят от констант, определенных в других классах
    // (Field.DEFAULT_COUNT и GomokuGameField.CELL_SIZE),
    // что может относиться к параметрам игрового поля.
    public MainForm(GameField aGameField)
    {
        /* Form's settings */
        setTitle("Connect6");
        //setBounds(DEFAULT_MARGIN, DEFAULT_MARGIN, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setBounds(DEFAULT_MARGIN, DEFAULT_MARGIN, 615, 615);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(aGameField, BorderLayout.CENTER);

        setVisible(true);
    }
}
