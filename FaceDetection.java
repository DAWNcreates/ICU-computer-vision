package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class FaceDetection extends javax.swing.JFrame {
    private DaemonThread myThread = null;
    VideoCapture webSource = null;
    Mat frame = new Mat();
    MatOfByte mem = new MatOfByte();
    CascadeClassifier faceDetector;
    MatOfRect faceDetections = new MatOfRect();

    class DaemonThread implements Runnable {
        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    if (webSource.grab()) {
                        try {
                            webSource.retrieve(frame);
                            Graphics g = jPanel1.getGraphics();

                            faceDetector.detectMultiScale(frame, faceDetections);
                            for (Rect rect : faceDetections.toArray()) {
                                Point p1 = new Point(rect.x, rect.y);
                                Point p2 = new Point(rect.x + rect.width, rect.y + rect.height);
                                Imgproc.rectangle(frame, p1, p2, new Scalar(0, 255, 0), 3);
                            }

                            Imgcodecs.imencode(".bmp", frame, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            BufferedImage buff = (BufferedImage) im;

                            if (g.drawImage(buff, 0, 0, jPanel1.getWidth(), jPanel1.getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                if (!runnable) {
                                    System.out.println("Paused .....");
                                    this.wait();
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println("Error: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public FaceDetection() {
        initComponents();
        loadCascadeClassifier();
    }

    private void loadCascadeClassifier() {
        try {
            java.net.URL xmlUrl = FaceDetection.class.getResource("haarcascade_frontalface_alt.xml");
            if (xmlUrl == null) {
                JOptionPane.showMessageDialog(this, "haar cascade file not found in resources!", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
                return;
            }

            java.nio.file.Path tempPath = java.nio.file.Files.createTempFile("haar", ".xml");
            tempPath.toFile().deleteOnExit();

            try (java.io.InputStream in = xmlUrl.openStream()) {
                java.nio.file.Files.copy(in, tempPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            faceDetector = new CascadeClassifier(tempPath.toString());
            if (faceDetector.empty()) {
                System.err.println("❌ Failed to load classifier from file.");
                JOptionPane.showMessageDialog(this, "Failed to load face detector.", "Critical Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } else {
                System.out.println("✅ Face detector loaded successfully.");
            }
        } catch (IOException e) {
            System.err.println("❌ IO Error loading cascade: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "IO error loading classifier.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Face Detection");
        setResizable(false);

        jPanel1.setBackground(java.awt.Color.BLACK);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );

        jButton1.setText("Start Camera");
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jButton2.setText("Pause");
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        jButton2.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(320, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(320, 320, 320))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        webSource = new VideoCapture(0);
        if (!webSource.isOpened()) {
            JOptionPane.showMessageDialog(this, "Cannot open webcam!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        myThread = new DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();

        jButton1.setEnabled(false);
        jButton2.setEnabled(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        myThread.runnable = false;
        jButton2.setEnabled(false);
        jButton1.setEnabled(true);

        if (webSource != null && webSource.isOpened()) {
            webSource.release();
        }
    }

    // Variables declaration
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration
}
