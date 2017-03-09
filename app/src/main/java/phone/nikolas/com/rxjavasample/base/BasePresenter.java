package phone.nikolas.com.rxjavasample.base;

/**
 * Created by Pleret on 3/8/2017.
 */

public abstract class BasePresenter<V,VM>  {

    protected V view;
    protected VM viewModel;

    public void setView(V view){
        this.view = view;
    }

    public void setViewModel(VM viewModel){
        this.viewModel = viewModel;
    }
}
