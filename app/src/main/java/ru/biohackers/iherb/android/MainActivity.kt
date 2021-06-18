package ru.biohackers.iherb.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.biohackers.iherb.android.data.Repository
import ru.biohackers.iherb.android.databinding.MainActivityBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)

        if (repository.isAuthorized()) {
            graph.setStartDestination(R.id.mainFragment)
        } else {
            // todo аторизация
            // graph.setStartDestination(R.id.splashFragment)
        }
        onPostResume() // this required when app starts after screen lock
        navController.graph = graph
    }

}
