package th.ac.lannapoly.polytechlanna

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.database.*
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.fragment_home.*
import th.ac.lannapoly.polytechlanna.model.News


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    lateinit var myRef: DatabaseReference

    var newses: ArrayList<News> = ArrayList()

    lateinit var adapter: FirebaseListAdapter<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("newses")

        myRef.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if(dataSnapshot != null){
                    newses = ArrayList()
                    for (newsSnapshot in dataSnapshot.children) {
                        val news = newsSnapshot.getValue(News::class.java)
                        if (news != null) {
                            newses.add(news)
                            Log.i("xxx",news.title)
                        }
                    }
                }
            }

        })

        adapter = object:FirebaseListAdapter<News>(context,News::class.java,R.layout.row_news,myRef){

            override fun populateView(v: View?, model: News?, position: Int) {
                val titleTextView = v!!.findViewById<TextView>(R.id.titleTextView)
                titleTextView.text = model!!.title

                val imageView = v!!.findViewById<ImageView>(R.id.imageView)
                Glide.with(context!!).load(model!!.imgUrl).into(imageView)
            }
        }

        listView.adapter = adapter

        listView.setOnItemClickListener{parent,virew,position,id ->
            val news = adapter.getItem(position)
            //Toast.makeText(context, news.imgUrl, Toast.LENGTH_SHORT).show()
            FinestWebView.Builder(activity!!).show(news.url!!)

        }


//        button.setOnClickListener{
//            myRef.setValue(editText.text.toString())
//        }

//        adapter = FirebaseListAdapter<News>(context,News::class.java,android.R.layout.simple_list_item_1,myRef){
//
//        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
