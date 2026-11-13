package com.russia.launcher.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.russia.game.R
import com.russia.launcher.config.Config

class MonitoringFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_monitoring, container, false)
        view.findViewById<TextView>(R.id.serverAddress).text =
            "${Config.SERVER_IP}:${Config.SERVER_PORT}"
        return view
    }
}