import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import org.javacogs.*;

/**
 * RotPad is a simple GUI application for performing rotation ciphers on plain text.
 */
public class RotPad extends JFrame {
   private static final String TITLE   = "RotPad";
   private static final String VERSION = "1.0";

   private static final String TRANSFORM_ROT13    = "ROT13";
   private static final String TRANSFORM_ROT13N5  = "ROT13N5";
   private static final String TRANSFORM_ROTASCII = "ROT-ASCII";

   private BorderLayout borderLayout1 = new BorderLayout();
   private JPanel jPanel1 = new JPanel();
   private JPanel jPanel2 = new JPanel();
   private JScrollPane jScrollPane1 = new JScrollPane();
   private CardLayout cardLayout1 = new CardLayout();
   private BorderLayout borderLayout2 = new BorderLayout();
   private JPanel jPanel3 = new JPanel();
   private JComboBox transformComboBox = new JComboBox();
   private JTextField rotationLengthField = new JTextField();
   private JTextArea textArea = new JTextArea();
   private JButton applyButton = new JButton();
   private JPanel jPanel4 = new JPanel();
   private JButton aboutButton = new JButton();
   private JButton hexButton = new JButton();
   private JButton unhexButton = new JButton();

   public RotPad() {
      try {
         // Initialize UI components.

         jbInit();

         // Position window in center of screen.

         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         setLocation(
            (screenSize.width - getSize().width) / 2,
            ((screenSize.height - getSize().height) / 2) - 25);

         // Add choices to mode combo box.

         transformComboBox.addItem(TRANSFORM_ROT13);
         transformComboBox.addItem(TRANSFORM_ROT13N5);
         transformComboBox.addItem(TRANSFORM_ROTASCII);
         transformComboBox.setSelectedIndex(0);

         // Add listener to Apply buttons.

         applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               applyTransform();
            }
         });

         // Add listeners to Hex and Unhex buttons.

         hexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               hexEncode();
            }
         });

         unhexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               hexDecode();
            }
         });

         // Add listener to About button.

         aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               showAbout();
            }
         });

         // Add listener to transform combo box.

         transformComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               setControls();
            }
         });
      }  catch(Exception ex) {
         System.out.println(ex);
      }
   }

   private void jbInit() throws Exception {
      this.setTitle(TITLE);
      this.setSize(new Dimension(500, 400));
      this.setDefaultCloseOperation(3);
      this.getContentPane().setLayout(borderLayout1);
      textArea.setWrapStyleWord(true);
      this.getContentPane().add(jPanel1, BorderLayout.CENTER);
      this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

      jPanel1.setLayout(cardLayout1);
      jPanel1.add(jScrollPane1, "jScrollPane1");

      jScrollPane1.setBorder(BorderFactory.createEtchedBorder());
      jScrollPane1.getViewport().add(textArea, null);

      textArea.setTabSize(3);
      textArea.setBorder(null);
      textArea.setLineWrap(true);

      jPanel2.setLayout(borderLayout2);
      jPanel2.setMinimumSize(new Dimension(304, 25));
      jPanel2.setPreferredSize(new Dimension(304, 25));
      jPanel2.add(jPanel3, BorderLayout.WEST);
      jPanel2.add(jPanel4, BorderLayout.EAST);

      jPanel3.setLayout(null);
      jPanel3.setMinimumSize(new Dimension(350, 25));
      jPanel3.setPreferredSize(new Dimension(350, 25));
      jPanel3.add(transformComboBox, null);
      jPanel3.add(rotationLengthField, null);
      jPanel3.add(hexButton, null);
      jPanel3.add(unhexButton, null);
      jPanel3.add(applyButton, null);

      transformComboBox.setMinimumSize(new Dimension(95, 23));
      transformComboBox.setPreferredSize(new Dimension(95, 23));
      transformComboBox.setBounds(new Rectangle(0, 2, 109, 23));

      rotationLengthField.setMinimumSize(new Dimension(4, 23));
      rotationLengthField.setPreferredSize(new Dimension(12, 23));
      rotationLengthField.setBounds(new Rectangle(111, 2, 43, 23));
      rotationLengthField.setEnabled(false);
      rotationLengthField.setText("1");

      applyButton.setMaximumSize(new Dimension(65, 23));
      applyButton.setMinimumSize(new Dimension(65, 23));
      applyButton.setPreferredSize(new Dimension(65, 23));
      applyButton.setMargin(new Insets(0, 0, 0, 0));
      applyButton.setText("Apply");
      applyButton.setBounds(new Rectangle(156, 2, 60, 23));

      hexButton.setBounds(new Rectangle(227, 2, 60, 23));
      hexButton.setText("Hex");
      hexButton.setMargin(new Insets(0, 0, 0, 0));
      hexButton.setPreferredSize(new Dimension(65, 23));
      hexButton.setMinimumSize(new Dimension(65, 23));
      hexButton.setMaximumSize(new Dimension(65, 23));

      unhexButton.setMaximumSize(new Dimension(65, 23));
      unhexButton.setMinimumSize(new Dimension(65, 23));
      unhexButton.setPreferredSize(new Dimension(65, 23));
      unhexButton.setMargin(new Insets(0, 0, 0, 0));
      unhexButton.setText("Unhex");
      unhexButton.setBounds(new Rectangle(289, 2, 60, 23));

      jPanel4.setLayout(null);
      jPanel4.setMinimumSize(new Dimension(25, 25));
      jPanel4.setPreferredSize(new Dimension(25, 25));
      jPanel4.add(aboutButton, null);

      aboutButton.setText("?");
      aboutButton.setBounds(new Rectangle(0, 2, 25, 23));
      aboutButton.setMargin(new Insets(0, 0, 0, 0));
      aboutButton.setPreferredSize(new Dimension(25, 23));
      aboutButton.setMinimumSize(new Dimension(25, 23));
      aboutButton.setMaximumSize(new Dimension(25, 23));
   }

   /**
    * Apply the selected transformation to the text.
    */
   private void applyTransform() {
      String transform = (String) transformComboBox.getSelectedItem();

      if (transform.equals(TRANSFORM_ROT13)) {
         applyRot13();
      } else if (transform.equals(TRANSFORM_ROT13N5)) {
         applyRot13n5();
      } else if (transform.equals(TRANSFORM_ROTASCII)) {
         applyRotAscii();
      } else {
         MessageDialog.show(this, "Unsupported transformation: " + transform);
      }
   }

   private void applyRot13() {
      Rot13 cipher = new Rot13();
      textArea.setText(cipher.transform(textArea.getText()));
   }

   private void applyRot13n5() {
      Rot13n5 cipher = new Rot13n5();
      textArea.setText(cipher.transform(textArea.getText()));
   }

   private void applyRotAscii() {
      int    rotLength = 0;
      String temp = "";

      try {
         temp = rotationLengthField.getText().trim();
         rotLength = Integer.parseInt(temp);
         RotAscii cipher = new RotAscii(rotLength);

         textArea.setText(cipher.transform(textArea.getText()));
      } catch(Exception ex) {
         ExceptionDialog.show(this, "Invalid rotation length: ", ex);
      }
   }

   private void hexEncode() {
      textArea.setText(StrUtil.hexEncode(textArea.getText()));
   }

   private void hexDecode() {
      textArea.setText(StrUtil.hexDecode(textArea.getText()));
   }

   private void setControls() {
      String transform = (String) transformComboBox.getSelectedItem();
      rotationLengthField.setEnabled(transform.equals(TRANSFORM_ROTASCII));
   }

   /**
    * Show the application indentification information.
    */
   private void showAbout() {
      MessageDialog.show(
         this,
         "About",
         TITLE + " v" + VERSION + "\n\n" +
            "Developed by Thornton Rose\n" +
            "Copyright 2000");
   }

   /**
    * Start the application.
    */
   public static void main(String[] args) {
      RotPad rotPad1 = new RotPad();
      rotPad1.show();
   }
}