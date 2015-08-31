package esa.carloscrespo.brainslug;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.*;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    static DatagramSocket datagramSocket;
    static InetAddress rtccAddress;
    static SendToRTCC sendToRTCC ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);



    }
    @Override
    protected void onStop(){
        Log.i("TAG","---------------------------Stopping Activity");
        super.onStop();
        sendToRTCC = new SendToRTCC();
        sendToRTCC.execute("LOST");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast toast = Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT);
            toast.show();
            /*try {
                rtccAddress=InetAddress.getByName("192.168.201");
                datagramSocket= new DatagramSocket(3333,rtccAddress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Toast toastF = Toast.makeText(getApplicationContext(), "Connection Failed... address not available", Toast.LENGTH_SHORT);
                toastF.show();
            } catch (SocketException e) {
                e.printStackTrace();
                Toast toastFF = Toast.makeText(getApplicationContext(), "Connection Failed... port not available", Toast.LENGTH_SHORT);
                toastFF.show();
            }*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class SendToRTCC extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            try {

                String udpPacket="1 RTPFSTC TR PFS ";
                for(String s: params){
                    udpPacket+=s;
                    udpPacket+=" ";
                }
                udpPacket+="\n";
                rtccAddress=InetAddress.getByName("192.168.1.201");
                datagramSocket= new DatagramSocket();
                DatagramPacket dp = new DatagramPacket(udpPacket.getBytes(),udpPacket.length(),rtccAddress,3333 );
                datagramSocket.send(dp);

            } catch (UnknownHostException e) {

                e.printStackTrace();
            } catch (SocketException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        TextView tMode;
        SeekBar sMode;
        TextView tVelo;
        SeekBar sVelo;
        TextView tAng;
        SeekBar sAng;
        TextView tVeloTurn;
        SeekBar sVeloTurn;
        boolean wheelsAligned;
        boolean firstPointTurn;
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onStop(){
            Log.i("TAG","---------------------------Stopping Fragment");

            super.onStop();
            sendToRTCC = new SendToRTCC();
            sendToRTCC.execute("LOST");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ((Button)rootView.findViewById(R.id.lost)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendToRTCC = new SendToRTCC();
                            sendToRTCC.execute("LOST");
                        }
                    }
            );

            tMode=(TextView) rootView.findViewById(R.id.tMode);
            sMode=(SeekBar) rootView.findViewById(R.id.sMode);
            tMode.setText((new Integer(sMode.getProgress())).toString());
            OnSeekBarChangeListener osbc=new OnSeekBarChangeListener() {
                Integer p;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    p=progress;
                    tMode.setText(p.toString());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //sMode.setProgress(p);
                }
            };
            sMode.setOnSeekBarChangeListener(osbc);

            ((Button)rootView.findViewById(R.id.roso)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendToRTCC = new SendToRTCC();
                            sendToRTCC.execute("ROSO", tMode.getText().toString());
                            firstPointTurn=true;
                        }
                    }
            );

            tVelo=(TextView) rootView.findViewById(R.id.tVelo);
            sVelo=(SeekBar) rootView.findViewById(R.id.sVelo);
            tVelo.setText((new Integer(sVelo.getProgress()-30)).toString());
            sVelo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                Integer p;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    p = progress - 30;
                    tVelo.setText(p.toString());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            tAng=(TextView) rootView.findViewById(R.id.tAng);
            sAng=(SeekBar) rootView.findViewById(R.id.sAng);
            tAng.setText((new Integer(sAng.getProgress()-12)).toString());
            sAng.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                Integer p;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    p = progress - 12;
                    tAng.setText(p.toString());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            ((Button)rootView.findViewById(R.id.lomc)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendToRTCC = new SendToRTCC();
                            sendToRTCC.execute("LOMC", tVelo.getText().toString(), tAng.getText().toString());
                            firstPointTurn=true;
                        }
                    }
            );

            wheelsAligned=false;
            firstPointTurn=true;
            tVeloTurn=(TextView) rootView.findViewById(R.id.tVeloTurn);
            sVeloTurn=(SeekBar) rootView.findViewById(R.id.sVeloTurn);
            tVeloTurn.setText((new Integer(sVeloTurn.getProgress()-30)).toString());
            sVeloTurn.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                Integer p;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    p = progress - 30;
                    tVeloTurn.setText(p.toString());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });



            ((Button)rootView.findViewById(R.id.lort)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wheelsAligned = (new Integer(sVeloTurn.getProgress() - 30)) == 0;

                            if (!wheelsAligned && firstPointTurn) {
                                sendToRTCC = new SendToRTCC();
                                sendToRTCC.execute("LORT", "0", "-30");
                                firstPointTurn = false;
                                Context context = getActivity().getApplicationContext();
                                CharSequence text = "Aligning wheels for point turn, please wait until the wheels are completely aligned";
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();

                            } else {
                                sendToRTCC = new SendToRTCC();
                                sendToRTCC.execute("LORT", tVeloTurn.getText().toString(), "-30");
                            }
                            if (wheelsAligned) {
                                firstPointTurn = false;
                            }
                        }
                    }
            );

            ((Button) rootView.findViewById(R.id.loho)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendToRTCC = new SendToRTCC();
                            sendToRTCC.execute("LOHO");
                            firstPointTurn=true;
                        }
                    }
            );

            return rootView;
        }
    }

}
