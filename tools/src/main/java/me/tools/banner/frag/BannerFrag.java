//package me.tools.banner.frag;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.util.List;
//
//import me.tools.R;
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link BannerFrag.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link BannerFrag#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class BannerFrag extends Fragment implements ViewPager.OnPageChangeListener {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    public static final int NOT_INDICATOR = 0;
//    public static final int CIRCLE_INDICATOR = 1;
//    public static final int NUM_INDICATOR = 2;
//    public static final int NUM_INDICATOR_TITLE = 3;
//    public static final int CIRCLE_INDICATOR_TITLE = 4;
//    public static final int LEFT = 5;
//    public static final int CENTER = 6;
//    public static final int RIGHT = 7;
//    private int mIndicatorMargin = 5;
//    private int mIndicatorWidth = 8;
//    private int mIndicatorHeight = 8;
//    private int mIndicatorSelectedResId = R.drawable.banner_youth_radius_gray;
//    private int mIndicatorUnselectedResId = R.drawable.banner_youth_radius_white;
//    private int bannerStyle = CIRCLE_INDICATOR;
//    private int count;
//    private int currentItem;
//    private int delayTime = 2000;
//    private int gravity = -1;
//    private boolean isAutoPlay = true;
//    private List<ImageView> imageViews;
//    private List<ImageView> indicatorImages;
//    private Context context;
//    private ViewPager viewPager;
//    private LinearLayout indicator;
//    private Handler handler = new Handler();
//    private OnBannerClickListener listener;
//    private OnLoadImageListener imageListener;
//    private String[] titles;
//    private TextView bannerTitle, numIndicator;
//    private View mView;//
//
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public BannerFrag() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BannerFrag.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static BannerFrag newInstance(String param1, String param2) {
//        BannerFrag fragment = new BannerFrag();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initView();
//        initData();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        mView = inflater.inflate(R.layout.banner_frag, container, false);
//        return mView;
//    }
//
//    private void initView() {
//        viewPager = (ViewPager) mView.findViewById(R.id.viewpager);
//        indicator = (LinearLayout) mView.findViewById(R.id.indicator);
//        bannerTitle = (TextView) mView.findViewById(R.id.bannerTitle);
//        numIndicator = (TextView) mView.findViewById(R.id.numIndicator);
//        handleTypedArray(context, attrs);
//    }
//
//    private void initData() {
//
//    }
//    private void handleTypedArray(Context context, AttributeSet attrs) {
//        if (attrs == null) {
//            return;
//        }
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
//        mIndicatorWidth =typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, 8);
//        mIndicatorHeight =typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, 8);
//        mIndicatorMargin =typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, 5);
//        mIndicatorSelectedResId =typedArray.getResourceId(R.styleable.Banner_indicator_drawable_selected,
//                R.drawable.banner_youth_radius_gray);
//        mIndicatorUnselectedResId =typedArray.getResourceId(R.styleable.Banner_indicator_drawable_unselected,
//                R.drawable.banner_youth_radius_white);
//        typedArray.recycle();
//    }
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    //ViewPager相关
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//    //end ViewPager
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//    public interface OnBannerClickListener {
//        void OnBannerClick(View view, int position);
//    }
//
//    public interface OnLoadImageListener {
//        void OnLoadImage(ImageView view, Object url);
//    }
//}
