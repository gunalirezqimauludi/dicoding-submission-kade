package com.gunalirezqi.footballclub.feature.detail.team.info

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.gunalirezqi.footballclub.R
import com.gunalirezqi.footballclub.api.ApiRepository
import com.gunalirezqi.footballclub.model.Team
import com.gunalirezqi.footballclub.utils.invisible
import com.gunalirezqi.footballclub.utils.visible
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.nestedScrollView

class TeamInfoFragment : Fragment(), TeamInfoView {

    private lateinit var presenter: InfoPresenter

    private lateinit var progressBar: ProgressBar

    private lateinit var teamId: String

    companion object {
        private const val ARG_LEAGUE_ID = ""

        fun newInstance(teamId: String): TeamInfoFragment {
            val args = Bundle()
            args.putString(ARG_LEAGUE_ID, teamId)
            val fragment = TeamInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            teamId = arguments?.getString(ARG_LEAGUE_ID) ?: "ARG_LEAGUE_ID"
        }

        return UI {

            // UI Layout
            nestedScrollView {
                isNestedScrollingEnabled = true
                relativeLayout {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            topPadding = dip(5)
                            bottomPadding = dip(5)

                            textView {
                                text = context.getString(R.string.label_team)
                                textSize = 18f
                                textColor = Color.parseColor("#008577")
                                setTypeface(typeface, Typeface.BOLD)
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_team_name)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.team_name
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_team_short_name)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.team_short_name
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_team_alternate_name)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.team_alternate_name
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            padding = dip(2)

                            textView {
                                topPadding = dip(8)
                                text = context.getString(R.string.label_team_description)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }

                            textView {
                                topPadding = dip(2)

                                id = R.id.team_description
                                textSize = 12f //sp
                                gravity = Gravity.FILL_HORIZONTAL
                            }
                        }

                        view {
                            backgroundColor = Color.LTGRAY
                        }.lparams(matchParent, dip(1)) {
                            topMargin = dip(8)
                            bottomMargin = dip(8)
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            bottomPadding = dip(5)

                            textView {
                                text = context.getString(R.string.label_team_stadium)
                                textSize = 18f
                                textColor = Color.parseColor("#008577")
                                setTypeface(typeface, Typeface.BOLD)
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_team_stadium_name)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.team_stadium_name
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            padding = dip(2)

                            textView {
                                text = context.getString(R.string.label_team_stadium_location)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }.lparams(width = dip(100), height = matchParent)

                            textView {
                                id = R.id.team_stadium_location
                                textSize = 12f //sp
                            }
                        }

                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            padding = dip(2)

                            textView {
                                topPadding = dip(8)

                                text = context.getString(R.string.label_team_stadium_description)
                                textSize = 12f //sp
                                setTypeface(typeface, Typeface.BOLD)
                            }

                            textView {
                                topPadding = dip(2)

                                id = R.id.team_stadium_description
                                textSize = 12f //sp
                                gravity = Gravity.FILL_HORIZONTAL
                            }
                        }

                        imageView {
                            id = R.id.team_stadium_thumb
                        }
                    }.lparams {
                        padding = dip(16)
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerInParent()
                    }
                }
            }

            // Presenter
            val request = ApiRepository()
            val gson = Gson()
            presenter = InfoPresenter(this@TeamInfoFragment, request, gson)
            presenter.getTeamInfo(teamId)
        }.view
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    @SuppressLint("SetTextI18n", "InlinedApi")
    override fun showTeamInfo(data: List<Team>) {

        // Get First Data
        val items = data.first()

        // Team Info
        val teamName: TextView = find(R.id.team_name)
        val teamShortName: TextView = find(R.id.team_short_name)
        val teamAlternateName: TextView = find(R.id.team_alternate_name)
        val teamDescription: TextView = find(R.id.team_description)

        // Stadium Info
        val teamStadiumName: TextView = find(R.id.team_stadium_name)
        val teamStadiumThumb: ImageView = find(R.id.team_stadium_thumb)
        val teamStadiumLocation: TextView = find(R.id.team_stadium_location)
        val teamStadiumDescription: TextView = find(R.id.team_stadium_description)

        teamName.text = ": " + (items.teamName ?: "-")
        teamShortName.text = ": " + (items.teamShortName ?: "-")
        teamAlternateName.text = ": " + (items.teamAlternateName ?: "-")
        teamDescription.text = (items.teamDescription ?: "-")
        teamDescription.justificationMode = JUSTIFICATION_MODE_INTER_WORD

        teamStadiumName.text = ": " + (items.teamStadiumName ?: "-")
        teamStadiumLocation.text = ": " + (items.teamStadiumLocation ?: "-")
        Picasso.get().load(items.teamStadiumThumb).into(teamStadiumThumb)
        teamStadiumDescription.text = (items.teamStadiumDescription ?: "-")
        teamStadiumDescription.justificationMode = JUSTIFICATION_MODE_INTER_WORD
    }
}
