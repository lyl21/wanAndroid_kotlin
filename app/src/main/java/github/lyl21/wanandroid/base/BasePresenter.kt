import github.lyl21.wanandroid.base.BaseView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers



open class BasePresenter<V : BaseView?> {

    private var compositeDisposable: CompositeDisposable? = null
    var mBaseView: V? = null
    protected var apiServer: API.WanAndroidApi = RetrofitService.getApiService()

    /**
     * 绑定
     */
    fun attachView(baseView: V) {
        mBaseView = baseView
    }

    /**
     * 解除绑定
     */
    fun detachView() {
        mBaseView = null
        removeDisposable()
    }

    /**
     * 返回 view
     */
    fun getBaseView(): V? {
        return mBaseView
    }

    fun addDisposable(observable: Observable<*>, observer: BaseObserver<Any>) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!
            .add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer)
            )
    }

    private fun removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
    }

}