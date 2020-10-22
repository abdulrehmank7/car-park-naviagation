package com.arkapp.carparknaviagation.ui.navigation;

import com.arkapp.carparknaviagation.R;
import com.here.android.mpa.routing.Maneuver;

/**
 * Created by Abdul Rehman on 16-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class Utils {

    public static int getNextManeuverIcon(Maneuver.Icon icon) {
        if (icon == null)
            return R.drawable.ic_nav_011_up_arrow;
        switch (icon) {
            case UNDEFINED:
                return R.drawable.ic_nav_011_up_arrow;

            case GO_STRAIGHT:
                return R.drawable.ic_nav_011_up_arrow;

            case UTURN_RIGHT:
                return R.drawable.ic_nav_004_u_turn;

            case UTURN_LEFT:
                return R.drawable.ic_nav_004_u_turn;

            case KEEP_RIGHT:
                return R.drawable.ic_nav_011_up_arrow;

            case LIGHT_RIGHT:
                return R.drawable.ic_nav_014_left_turn_r;

            case QUITE_RIGHT:
                return R.drawable.ic_nav_003_right_arrow;

            case HEAVY_RIGHT:
                return R.drawable.ic_nav_008_left_turn_r;

            case KEEP_MIDDLE:
                return R.drawable.ic_nav_011_up_arrow;

            case KEEP_LEFT:
                return R.drawable.ic_nav_011_up_arrow;

            case LIGHT_LEFT:
                return R.drawable.ic_nav_014_left_turn;

            case QUITE_LEFT:
                return R.drawable.ic_nav_001_left_arrow;

            case ENTER_HIGHWAY_RIGHT_LANE:
                return R.drawable.ic_nav_003_right_arrow;

            case ENTER_HIGHWAY_LEFT_LANE:
                return R.drawable.ic_nav_001_left_arrow;

            case LEAVE_HIGHWAY_RIGHT_LANE:
                return R.drawable.ic_nav_003_right_arrow;

            case LEAVE_HIGHWAY_LEFT_LANE:
                return R.drawable.ic_nav_001_left_arrow;

            case HIGHWAY_KEEP_RIGHT:
                return R.drawable.ic_nav_011_up_arrow;

            case ROUNDABOUT_1:
            case ROUNDABOUT_2:
            case ROUNDABOUT_3:
            case ROUNDABOUT_4:
            case ROUNDABOUT_5:
            case ROUNDABOUT_6:
            case ROUNDABOUT_7:
            case ROUNDABOUT_8:
            case ROUNDABOUT_9:
            case ROUNDABOUT_10:
            case ROUNDABOUT_11:
            case ROUNDABOUT_12:
            case ROUNDABOUT_1_LH:
            case ROUNDABOUT_2_LH:
            case ROUNDABOUT_3_LH:
            case ROUNDABOUT_4_LH:
            case ROUNDABOUT_5_LH:
            case ROUNDABOUT_6_LH:
            case ROUNDABOUT_7_LH:
            case ROUNDABOUT_8_LH:
            case ROUNDABOUT_9_LH:
            case ROUNDABOUT_10_LH:
            case ROUNDABOUT_11_LH:
            case ROUNDABOUT_12_LH:
                return R.drawable.ic_nav_007_roundabout;

            case START:
                return R.drawable.ic_nav_011_up_arrow;

            case END:
                return R.drawable.ic_nav_037_finish;

            case FERRY:
                return R.drawable.ic_nav_031_ferry;

            case PASS_STATION:
                return R.drawable.ic_nav_042_pin;

            case HEAD_TO:
                return R.drawable.ic_nav_011_up_arrow;

            case CHANGE_LINE:
                return R.drawable.ic_nav_012_change;
            default:
                return R.drawable.ic_nav_011_up_arrow;

        }
    }

    public static String getTurnName(Maneuver.Turn turn) {
        String newValue = "";
        if (turn == null) {return newValue;}

        switch (turn) {
            case UNDEFINED:
                newValue = "";
                break;

            case NO_TURN:
                newValue = "No Turns";
                break;
            case KEEP_MIDDLE:
                newValue = "Keep In Middle";
                break;
            case KEEP_RIGHT:
                newValue = "Keep In Right";

                break;
            case LIGHT_RIGHT:
                newValue = "Turn Right Ahead";
                break;
            case QUITE_RIGHT:
                newValue = "Turn Right Ahead";
                break;
            case HEAVY_RIGHT:
                newValue = "Turn Right Ahead";
                break;
            case KEEP_LEFT:
                newValue = "Keep In Left";
                break;
            case LIGHT_LEFT:
                newValue = "Turn Left Ahead";
                break;
            case QUITE_LEFT:
                newValue = "Turn Left Ahead";
                break;
            case HEAVY_LEFT:
                newValue = "Turn Left Ahead";
                break;
            case RETURN:
                newValue = "Retun";
                break;
            case ROUNDABOUT_1:
            case ROUNDABOUT_2:
            case ROUNDABOUT_3:
            case ROUNDABOUT_4:
            case ROUNDABOUT_5:
            case ROUNDABOUT_6:
            case ROUNDABOUT_7:
            case ROUNDABOUT_8:
            case ROUNDABOUT_9:
            case ROUNDABOUT_10:
            case ROUNDABOUT_11:
            case ROUNDABOUT_12:
                newValue = "Roundabout Ahead";
                break;
            default:
                newValue = "";
        }
        return newValue;
    }
}
