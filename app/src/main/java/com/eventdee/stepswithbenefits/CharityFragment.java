package com.eventdee.stepswithbenefits;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CharityFragment extends Fragment {

    private CharityAdapter mCharityAdapter;
    private ArrayList<Charity> mCharityArray = new ArrayList<>();
    private RecyclerView mPhotosRecyclerView;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
//    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharityFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CharityFragment newInstance(int columnCount) {
        CharityFragment fragment = new CharityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        mCharityArray.add(new Charity("100 People Doing Good", "The Power of 100 People", R.drawable.hundred_people_doing_good, R.drawable.background_100_people));
        mCharityArray.add(new Charity("Action for AIDS", "Doing It Better", R.drawable.logo_afa, R.drawable.background_afa));
        mCharityArray.add(new Charity("Red Cross Singapore", "Donate Blood, Start Young", R.drawable.logo_red_cross, R.drawable.background_red_cross));
        mCharityArray.add(new Charity("Animal Concern Research and Education Society", "A world where animals are treated with compassion and respect.", R.drawable.logo_acres, R.drawable.background_acres));
        mCharityArray.add(new Charity("Make A Wish Foundation", "To reach every medically eligible child in Singapore", R.drawable.logo_make_a_wish, R.drawable.background_make_a_wish));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charity_list, container, false);

        mCharityAdapter = new CharityAdapter(getContext());
        mCharityAdapter.setCharityArray(mCharityArray);

        mPhotosRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mPhotosRecyclerView.setAdapter(mCharityAdapter);

//        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mPhotosRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));



        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
////            recyclerView.setAdapter(new CharityRecyclerViewAdapter(DummyContent.ITEMS, mListener));
//        }
        return view;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
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
//    public interface OnListFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction(DummyItem item);
//    }
}
