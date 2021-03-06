package com.trycarriage.test.ui.activities.users.repos

import com.trycarriage.test.application.helpers.rx.SchedulerProvider
import com.trycarriage.test.data.DataManager
import com.trycarriage.test.data.remote.api.models.users.repos.req.RequestUserRepos
import com.trycarriage.test.data.remote.api.models.users.repos.resp.Repo
import com.trycarriage.test.ui.base.arch.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * @name Test
 * Copyrights (c) 12/12/18 Created By Ahmad Najar
 **/
class UserReposViewModel<N : UserReposNavigator>(
    dataManager: DataManager, compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<N>(dataManager, compositeDisposable, schedulerProvider) {

    var data: List<Repo>? = null

    companion object {
        const val ACCOUNT_NAME = "mralexgray"
    }

    fun getRepos() {
        if (!getNavigator().isConnected()) {
            getNavigator().showNoInternetConnection()
            return
        }
        getNavigator().showLoading()
        if (data != null) {
            getNavigator().showRepos(data)
            return
        }
        compositeDisposable.add(
            dataManager.getRepos(RequestUserRepos(ACCOUNT_NAME))
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe({
                    data = it
                    getNavigator().showRepos(it)
                }, ::handleError)
        )


    }

    fun onRefresh() {
        if (!getNavigator().isConnected()) {
            getNavigator().showNoInternetConnection()
            return
        }
        data = null
        getRepos()
    }


}