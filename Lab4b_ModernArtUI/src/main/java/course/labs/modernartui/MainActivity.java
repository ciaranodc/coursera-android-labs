package course.labs.modernartui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.labs.modernartui.R;

public class MainActivity extends AppCompatActivity {

    // Views
    private SurfaceView tileTopLeft;
    private SurfaceView tileBottomLeft;
    private SurfaceView tileTopRight;
    private SurfaceView tileCentreRight;
    private SurfaceView tileBottomRight;

    private SeekBar seekBar;

    // Colors
    private int topLeftColorFrom;
    private int topLeftColorTo;
    private int bottomLeftColorFrom;
    private int bottomLeftColorTo;
    private int topRightColorFrom;
    private int topRightColorTo;
    private int bottomRightColorFrom;
    private int bottomRightColorTo;

    // Color Animators
    private ValueAnimator colorAnimationTopLeft;
    private ValueAnimator colorAnimationBottomLeft;
    private ValueAnimator colorAnimationTopRight;
    private ValueAnimator colorAnimationBottomRight;

    // Dialog
    private VisitWebsiteDialog dialog = new VisitWebsiteDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initColors();
        initViews();
        initAnimations();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColorAnimations(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Modern Art UI");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    private void initColors() {
        topLeftColorFrom = getResources().getColor(R.color.blueLight);
        topLeftColorTo = getResources().getColor(R.color.beigeLight);
        bottomLeftColorFrom = getResources().getColor(R.color.pink);
        bottomLeftColorTo = getResources().getColor(R.color.beige);
        topRightColorFrom = getResources().getColor(R.color.redDark);
        topRightColorTo = getResources().getColor(R.color.orangeDark);
        bottomRightColorFrom = getResources().getColor(R.color.blueDark);
        bottomRightColorTo = getResources().getColor(R.color.greenPale);
    }

    private void initViews() {
        tileTopLeft = findViewById(R.id.top_left);
        tileBottomLeft = findViewById(R.id.bottom_left);
        tileTopRight = findViewById(R.id.top_right);
        tileCentreRight = findViewById(R.id.centre_right);
        tileBottomRight = findViewById(R.id.bottom_right);

        tileTopLeft.setBackgroundResource(R.color.blueLight);
        tileBottomLeft.setBackgroundResource(R.color.pink);
        tileTopRight.setBackgroundResource(R.color.redDark);
        tileCentreRight.setBackgroundResource(R.color.white);
        tileBottomRight.setBackgroundResource(R.color.blueDark);

        seekBar = findViewById(R.id.seek_bar);
    }

    private void initAnimations() {
        colorAnimationTopLeft = ValueAnimator.ofObject(new ArgbEvaluator(), topLeftColorFrom, topLeftColorTo);
        colorAnimationBottomLeft = ValueAnimator.ofObject(new ArgbEvaluator(), bottomLeftColorFrom, bottomLeftColorTo);
        colorAnimationTopRight = ValueAnimator.ofObject(new ArgbEvaluator(), topRightColorFrom, topRightColorTo);
        colorAnimationBottomRight = ValueAnimator.ofObject(new ArgbEvaluator(), bottomRightColorFrom, bottomRightColorTo);

        colorAnimationTopLeft.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                tileTopLeft.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        colorAnimationBottomLeft.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                tileBottomLeft.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        colorAnimationTopRight.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                tileTopRight.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        colorAnimationBottomRight.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                tileBottomRight.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
    }

    private void updateColorAnimations(int level) {
        float fraction = (float) level / 100;

        colorAnimationTopLeft.setCurrentFraction(fraction);
        colorAnimationBottomLeft.setCurrentFraction(fraction);
        colorAnimationTopRight.setCurrentFraction(fraction);
        colorAnimationBottomRight.setCurrentFraction(fraction);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_style, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more_info) {
            //open dialog
            dialog.show(getSupportFragmentManager(), "moma");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
