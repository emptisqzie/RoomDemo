import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roomdemo.Product
import com.example.roomdemo.ProductDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductRepository(
    private val productDao: ProductDao
) {
    val allProducts: LiveData<List<Product>> =
        productDao.getAllProducts()

    val searchResults = MutableLiveData<List<Product>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertProduct(newproduct: Product) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.insertProduct(newproduct)
        }
    }

    fun deleteProduct(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(name)
        }
    }

    fun findProduct(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<Product>> {
        return coroutineScope.async(Dispatchers.IO) {
            productDao.findProduct(name)
        }
    }
}