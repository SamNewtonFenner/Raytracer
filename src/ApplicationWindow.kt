import javax.swing.JFrame

class ApplicationWindow(title: String, setWidth: Int, setHeight: Int): JFrame() {

    init {
        createUI(title, setWidth, setHeight);
    }

    private fun createUI(title: String, setWidth: Int, setHeight: Int) {

        setTitle(title);

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE;
        setSize(setWidth, setHeight);
        setLocationRelativeTo(null);
        isResizable = false;
    }
}
