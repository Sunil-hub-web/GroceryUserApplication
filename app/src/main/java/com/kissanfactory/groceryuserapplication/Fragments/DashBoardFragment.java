package com.kissanfactory.groceryuserapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Activities.AllCategoriesActivity;
import com.kissanfactory.groceryuserapplication.Activities.CategoryDetailActivity;
import com.kissanfactory.groceryuserapplication.Activities.SearchActivity;
import com.kissanfactory.groceryuserapplication.Adapters.CategoryProductAdapter;
import com.kissanfactory.groceryuserapplication.Adapters.DashboardCategoriesAdapter;
import com.kissanfactory.groceryuserapplication.Adapters.TopCategoriesHeadingAdapter;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Category;
import com.kissanfactory.groceryuserapplication.Models.Favourite;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.Models.SlideShow;
import com.kissanfactory.groceryuserapplication.Models.SlideShowResponse;
import com.kissanfactory.groceryuserapplication.Models.top_productsdto.Datum;
import com.kissanfactory.groceryuserapplication.Models.top_productsdto.TopProductDTO;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DashBoardFragment extends Fragment {

    private RecyclerView categories, vegProducts, snackProducts, houseProducts, fruitProducts, rv_top_products;
    private TextView viewAllCat, viewAllVeg, viewAllFruits, viewAllHouse, viewAllSnacks;
    private List<Favourite> favourites;
    private RelativeLayout fruitsHolder, vegHolder, snackHolder, houseHolder;
    SwipeRefreshLayout swipeRefreshLayout;

    private SharedPreferences sharedPreferences;

    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init(view);
            }
        });
        init(view);
        return view;
    }

    private void init(View view) {

        fruitsHolder = view.findViewById(R.id.fruitHolder);
        vegHolder = view.findViewById(R.id.vegHolder);
        fruitsHolder = view.findViewById(R.id.houseHolder);
        snackHolder = view.findViewById(R.id.snackHolder);


        getSlideShow(view);
        searchBox(view);
        categories = view.findViewById(R.id.categoriesList);
        vegProducts = view.findViewById(R.id.vegProducts);
        snackProducts = view.findViewById(R.id.snackProduct);
        houseProducts = view.findViewById(R.id.houseProducts);
        fruitProducts = view.findViewById(R.id.fruitProduct);

        viewAllButtons(view);

        sharedPreferences = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("exist", false)) {
            getFavourites();
        } else {
            getProducts();
        }

        fillCategories();

        Top_categories(view);
    }

    // getting the products from each category
    private void getProducts() {
        // root views

        fillCategoryProducts(fruitProducts, "6112b0b4afe06f7494047ae3", fruitsHolder);
        fillCategoryProducts(vegProducts, "610d672f3a335f1aff3c367b", vegHolder);
        fillCategoryProducts(snackProducts, "61116e4aafe06f7494047ab7", snackHolder);
        fillCategoryProducts(houseProducts, "611114586a8701fec2bdb80e", houseHolder);
    }

    private void Top_categories(View view) {
        rv_top_products = view.findViewById(R.id.rv_top_products);

//        TopCategoriesHeadingAdapter topCategoriesHeadingAdapter = new TopCategoriesHeadingAdapter(DashBoardFragment.this.getActivity());
//        rv_top_products.setAdapter(topCategoriesHeadingAdapter);
//        rv_top_products.setLayoutManager(new LinearLayoutManager(DashBoardFragment.this.getContext(), RecyclerView.VERTICAL, false));

        call_Top_Product_API(rv_top_products);

    }

    private void call_Top_Product_API(RecyclerView view) {
        Call<TopProductDTO> call = new UserApiToJsonHandler().Top_Products_Api();
        call.enqueue(new Callback<TopProductDTO>() {
            @Override
            public void onResponse(Call<TopProductDTO> call, Response<TopProductDTO> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().getData() != null) {
                        TopCategoriesHeadingAdapter adapter = new TopCategoriesHeadingAdapter(DashBoardFragment.this.getActivity());

                        List<Datum> productList;
                        if (response.body().getData().size() > 5) {
                            productList = response.body().getData().subList(0, 5);
                        } else {
                            productList = response.body().getData();
                        }
                        adapter.setProductList(productList);


                        view.setAdapter(adapter);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardFragment.this.getActivity(),
                                RecyclerView.VERTICAL, false);
                        if (view.getItemDecorationCount() <= 0) {
                            view.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    super.getItemOffsets(outRect, view, parent, state);
                                    outRect.left = 20;
                                }
                            });
                        }

                        view.setLayoutManager(layoutManager);
                    } else {
                        // hide that category
//                        holder..setVisibility(View.GONE);
                    }
                } else {
                    Log.e("Error: ", "Something went bad!");
                }
            }

            @Override
            public void onFailure(Call<TopProductDTO> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e("Error: ", t.getMessage());
            }
        });


    }

    // category button functions
    private void viewAllButtons(View view) {
        // explore more categories text
        view.findViewById(R.id.exploreTv).setOnClickListener(v -> startActivity(new Intent(this.getActivity(), AllCategoriesActivity.class)));
        // view all categories
        viewAllCat = view.findViewById(R.id.viewAllCat);
        viewAllCat.setOnClickListener(view1 -> startActivity(new Intent(this.getActivity(), AllCategoriesActivity.class)));

        // view all veg
        viewAllVeg = view.findViewById(R.id.viewAllVeg);
        viewAllVeg.setOnClickListener(view1 -> startActivity(new Intent(this.getActivity(), CategoryDetailActivity.class)
                .putExtra("category", "610d672f3a335f1aff3c367b")
                .putExtra("name", "Vegetables")));

        // view all snacks
        viewAllSnacks = view.findViewById(R.id.viewAllSnacks);
        viewAllSnacks.setOnClickListener(view1 -> startActivity(new Intent(this.getActivity(), CategoryDetailActivity.class)
                .putExtra("category", "61116e4aafe06f7494047ab7")
                .putExtra("name", "Snacks")));

        // view all fruits
        viewAllFruits = view.findViewById(R.id.viewAllFruits);
        viewAllFruits.setOnClickListener(view1 -> startActivity(new Intent(this.getActivity(), CategoryDetailActivity.class)
                .putExtra("category", "6112b0b4afe06f7494047ae3")
                .putExtra("name", "Fruits")));

        // view all household items
        viewAllHouse = view.findViewById(R.id.viewAllHouse);
        viewAllHouse.setOnClickListener(view1 -> startActivity(new Intent(this.getActivity(), CategoryDetailActivity.class)
                .putExtra("category", "611114586a8701fec2bdb80e")
                .putExtra("name", "Household Items")));
    }

    // filling up the categories list
    private void fillCategories() {
        List<Category> categoriesLi = new ArrayList<>();
        categoriesLi.add(new Category("Products from village", "60687ef9ba160877d8ce5570"));
        categoriesLi.add(new Category("Agriculture - Feritilizer and Pesticide", "60687f10ba160877d8ce5571"));
        categoriesLi.add(new Category("Agriculture - Seeds", "60687f1aba160877d8ce5572"));
        categoriesLi.add(new Category("Agriculture - Animal Feed", "60687f21ba160877d8ce5573"));
        categoriesLi.add(new Category("Home Made Products", "60687f2cba160877d8ce5574"));

        DashboardCategoriesAdapter adapter = new DashboardCategoriesAdapter(DashBoardFragment.this.getContext());
        adapter.setCategories(categoriesLi);

        categories.setAdapter(adapter);
        categories.setLayoutManager(new LinearLayoutManager(DashBoardFragment.this.getContext(), RecyclerView.HORIZONTAL, false));
    }

    // filling the products in the categories
    private void fillCategoryProducts(RecyclerView view, String category, RelativeLayout holder) {

        Call<ApiResponse> call = new UserApiToJsonHandler().categoryProducts(category);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().getProductDetails() != null) {
                        CategoryProductAdapter adapter = new CategoryProductAdapter(DashBoardFragment.this.getActivity());

                        List<Product> productList;
                        if (response.body().getProductDetails().size() > 5) {
                            productList = response.body().getProductDetails().subList(0, 5);
                        } else {
                            productList = response.body().getProductDetails();
                        }

                        if (sharedPreferences.getBoolean("exist", false)) {
                            adapter.setProductList(getProductsWithFavourites(productList));
                        } else {
                            adapter.setProductList(productList);
                        }

                        adapter.setProductList(productList);

                        adapter.setOnItemClickListener(position -> {
                            if (sharedPreferences.getBoolean("exist", false)) {
                                ProgressDialog dialog = new ProgressDialog(DashBoardFragment.this.getActivity());
                                dialog.setCancelable(false);
                                dialog.setTitle("Adding to Favourites");
                                dialog.setMessage("Please wait...");
                                dialog.create();

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
                                String token = sharedPreferences.getString("token", "");
                                if (!productList.get(position).isLiked()) {
                                    dialog.show();

                                    productList.get(position).setProductId();
                                    Call<NoMsgResponse> call1 = new UserApiToJsonHandler().addToFavourites(token, productList.get(position));
                                    call1.enqueue(new Callback<NoMsgResponse>() {
                                        @Override
                                        public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                                            dialog.dismiss();
                                            if (response.isSuccessful()) {
                                                Toast.makeText(DashBoardFragment.this.getActivity(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                                                // add this favourite to the list
                                                Favourite favourite = new Favourite();
                                                favourite.setProductId(productList.get(position));
                                                favourite.setId(response.body().getFavourite().get_id());
                                                favourites.add(favourite);
                                                fillCategoryProducts(view, category, holder);
                                            } else {
                                                Gson gson = new Gson();
                                                ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                                                Toast.makeText(DashBoardFragment.this.getActivity(), message.getMsg(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                                            dialog.dismiss();
                                            Toast.makeText(DashBoardFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // dislike it here
                                    dialog.setTitle("Removing From Favourites");
                                    dialog.show();

                                    Call<ApiResponse> removeFav = new UserApiToJsonHandler().deleteFavourite(token, getFavouriteId(productList.get(position).getId()));
                                    removeFav.enqueue(new Callback<ApiResponse>() {
                                        @Override
                                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                            dialog.dismiss();
                                            if (response.isSuccessful()) {
                                                Toast.makeText(DashBoardFragment.this.getActivity(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
                                                removeFromFavourites(productList.get(position));
                                                fillCategoryProducts(view, category, holder);
                                            } else {
                                                Gson gson = new Gson();
                                                ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                                                Toast.makeText(DashBoardFragment.this.getActivity(), message.getMsg(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                                            dialog.dismiss();
                                            Toast.makeText(DashBoardFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(DashBoardFragment.this.getActivity(), "Login to add favourites", Toast.LENGTH_SHORT).show();
                            }

                        });

                        view.setAdapter(adapter);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardFragment.this.getActivity(),
                                RecyclerView.HORIZONTAL, false);
                        if (view.getItemDecorationCount() <= 0) {
                            view.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    super.getItemOffsets(outRect, view, parent, state);
                                    outRect.left = 20;
                                }
                            });
                        }

                        view.setLayoutManager(layoutManager);
                    } else {
                        // hide that category
//                        holder..setVisibility(View.GONE);
                    }
                } else {
                    Log.e("Error: ", "Something went bad!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e("Error: ", t.getMessage());
            }
        });


    }

    private String getFavouriteId(String prodId) {
        for (Favourite favourite : favourites) {
            if (favourite.getProductId().getId().equals(prodId)) {
                prodId = favourite.getId();
                break;
            }
        }
        return prodId;
    }

    private void removeFromFavourites(Product product) {
        for (Favourite favourite : favourites) {
            if (favourite.getProductId().getId().equals(product.getId())) {
                favourites.remove(favourite);
                break;
            }
        }
    }

    // get products after checking favourites
    private List<Product> getProductsWithFavourites(List<Product> productList) {
        if (favourites.size() >= 1 && favourites.get(0).getProductId() != null) {
            for (int i = 0; i < favourites.size(); i++) {
                for (Product product : productList) {
                    try {
                        if (favourites.get(i).getProductId().getId().equals(product.getId())) {
                            product.setLiked(true);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return productList;
    }

    // get the slide show images
    private void getSlideShow(View view) {
        ImageCarousel carousel = view.findViewById(R.id.carousel);
        carousel.setAutoPlay(true);
        Call<SlideShowResponse> call = new UserApiToJsonHandler().getSlideShow();
        call.enqueue(new Callback<SlideShowResponse>() {
            @Override
            public void onResponse(Call<SlideShowResponse> call, Response<SlideShowResponse> response) {
                if (response.isSuccessful()) {
                    List<CarouselItem> imgUrls = new ArrayList<>();
//                    Log.d("Slideshow", response.body().getData().toString());
                    for (SlideShow slideShow : response.body().getData()) {
                        imgUrls.add(new CarouselItem(slideShow.getImages().get(0)));
                    }
                    // pass the images string into the carousel
                    carousel.addData(imgUrls);
                }
            }

            @Override
            public void onFailure(Call<SlideShowResponse> call, Throwable t) {

            }
        });
    }

    // search box
    private void searchBox(View view) {
        EditText search = view.findViewById(R.id.productSearchBox);

        TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (!TextUtils.isEmpty(search.getText().toString().trim())) {
                    startActivity(new Intent(DashBoardFragment.this.getActivity(), SearchActivity.class)
                            .putExtra("term", search.getText().toString().trim()));
                }
                return true;
            }
        };
        search.setOnEditorActionListener(actionListener);
    }

    private void getFavourites() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<ApiResponse> call = new UserApiToJsonHandler().getFavourites(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    favourites = response.body().getFavourite();
                    getProducts();
                } else {
                    Log.e("Fav", "Smoethig went wrong");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Fav", t.getMessage());

            }
        });
    }


}