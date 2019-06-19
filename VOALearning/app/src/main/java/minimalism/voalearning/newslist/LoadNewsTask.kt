package minimalism.voalearning.newslist

import android.os.AsyncTask

class LoadNewsTask(var mFragment: NewsListAdapter): AsyncTask<String, Void, Void>() {

    override fun doInBackground(vararg params: String?): Void? {
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
    }

}