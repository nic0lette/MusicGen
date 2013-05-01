
package co.cutely.musicgen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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

    private StringMusic _currentMsg;

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
                final String msg = txtMessage.getText();

                if (_currentMsg == null || !_currentMsg.equals(msg)) {
                    _currentMsg = new StringMusic(msg);
                }
                _currentMsg.play();
            }
        });

        JButton btnEncode = new JButton("Save...");
        btnEncode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                final String msg = txtMessage.getText();
                if (_currentMsg == null || !_currentMsg.equals(msg)) {
                    _currentMsg = new StringMusic(msg);
                }

                final JFileChooser saveDialog = new JFileChooser(System.getProperty("user.home"));
                final int ret = saveDialog.showSaveDialog(MusicGen.this);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    final File savePath = saveDialog.getSelectedFile();
                    createMp3(savePath);
                }
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
                                        .addGroup(
                                                groupLayout.createSequentialGroup().addComponent(btnPlay).addPreferredGap(ComponentPlacement.RELATED)
                                                        .addComponent(btnEncode))).addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(
                groupLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblMessage)
                                        .addComponent(txtMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnPlay).addComponent(btnEncode))
                        .addContainerGap()));
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

    private void createMp3(final File file) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("C:\\Program Files (x86)\\Lame For Audacity\\lame.exe -r - " + file);
            InputStream in = p.getInputStream();
            OutputStream out = p.getOutputStream();

            // Encode everything
            _currentMsg.encode(out);

            // Signal the end of the stream by closing the processes's STDIN
            out.close();

            final BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
            String line = inReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = inReader.readLine();
            }

            // Wait
            p.waitFor();

            // And done
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
