
package co.cutely.musicgen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MusicGen extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String APP_NAME = MusicGen.class.getSimpleName();
    private JTextField txtMessage;

    private MusicGen() {
        this.setTitle(APP_NAME);
        this.setSize(314, 163);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblMessage = new JLabel("Message:");

        txtMessage = new JTextField();
        txtMessage.setColumns(10);

        JButton btnPlay = new JButton("Play");
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                new StringMusic(txtMessage.getText());
            }
        });
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                groupLayout
                                        .createParallelGroup(Alignment.LEADING)
                                        .addGroup(
                                                groupLayout.createSequentialGroup().addComponent(lblMessage)
                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                        .addComponent(txtMessage, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                                        .addComponent(btnPlay)).addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblMessage)
                                        .addComponent(txtMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE).addComponent(btnPlay).addContainerGap()));
        getContentPane().setLayout(groupLayout);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }

        new MusicGen(); // Create and show the GUI.
    }
}
