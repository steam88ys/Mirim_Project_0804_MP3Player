package kr.hs.emirim.s2102.mirim_project_0804_mp3player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView list1;
    Button btnPlay, btnPause, btnStop;
    TextView textMusic;
    ProgressBar proBar;
    ArrayList<String> musicList;
    String selectedMusic;

    String musicPath = Environment.getExternalStorageDirectory().getPath()+"/";
    MediaPlayer mediaPlayer;
    int seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MP3 Player");
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};    // 외부 저장소에 있는걸 사용하겠다고 지정
        ActivityCompat.requestPermissions(this, permissions, MODE_PRIVATE);
        list1 = findViewById(R.id.list1);
        btnPlay = findViewById(R.id.btn_play);
        btnStop = findViewById(R.id.btn_stop);
        btnPause = findViewById(R.id.btn_pause);
        textMusic = findViewById(R.id.text_music);
        proBar = findViewById(R.id.pro_bar);
        musicList = new ArrayList<String>();
        File[] files = new File(musicPath).listFiles();
        String fileName, extName;
        for (File file : files) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length()-3);
            if (extName.equals("mp3")) {
                musicList.add(fileName);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, musicList);
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list1.setAdapter(adapter);
        list1.setItemChecked(0, true);

        // 클릭했을 때 실행
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMusic = musicList.get(i);
            }
        });
        selectedMusic = musicList.get(0);   // 처음 생성될 때 초기값

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnPause.getText().equals("일시정지")) {
                    mediaPlayer.pause();
                    btnPause.setText("이어듣기");
                    btnPlay.setClickable(true);
                    btnStop.setClickable(true);
                    btnPause.setClickable(true);
                    proBar.setVisibility(View.INVISIBLE);
                }else {
                    mediaPlayer.start();
                    btnPause.setText("일시정지");
                    btnPlay.setClickable(true);
                    btnStop.setClickable(true);
                    btnPause.setClickable(true);
                    proBar.setVisibility(View.VISIBLE);
                }
            }
        });
        btnPause.setClickable(false);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(musicPath + selectedMusic);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    btnPlay.setClickable(false);
                    btnStop.setClickable(true);
                    btnPause.setClickable(true);
                    textMusic.setText("실행중인 음악: "+selectedMusic);
                    proBar.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                btnPlay.setClickable(true);
                btnStop.setClickable(false);
                btnPause.setClickable(false);
                textMusic.setText("실행중인 음악: ");
                proBar.setVisibility(View.INVISIBLE);
            }
        });
        btnStop.setClickable(false);
    }
}