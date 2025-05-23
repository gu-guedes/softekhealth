package com.example.softekhealth.presentation.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.softekhealth.domain.model.SymptomFrequency
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun FormsScreen(
    onFormComplete: () -> Unit,
    viewModel: FormsViewModel
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(initialPage = state.currentPage)
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Sincronizar o estado do pager com o ViewModel
    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.scrollToPage(state.currentPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (state.currentPage != pagerState.currentPage) {
            viewModel.onEvent(FormsEvent.GoToPage(pagerState.currentPage))
        }
    }

    // Notificação de sucesso ou erro
    LaunchedEffect(state.isCompleted, state.error) {
        if (state.isCompleted) {
            snackbarHostState.showSnackbar("Questionário enviado com sucesso!")
            onFormComplete()
        } else if (state.error != null) {
            snackbarHostState.showSnackbar("Erro: ${state.error}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Autoavaliação") },
                navigationIcon = {
                    IconButton(onClick = onFormComplete) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (state.currentPage == state.totalPages - 1) {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(FormsEvent.Submit) },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Enviar questionário",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Barra de progresso
            LinearProgressIndicator(
                progress = { (state.currentPage + 1).toFloat() / state.totalPages },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )

            // Pager para as diferentes páginas do questionário
            HorizontalPager(
                count = state.totalPages,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    when (page) {
                        0 -> StressLevelPage(state, viewModel, scrollState)
                        1 -> SymptomsSelectionPage(state, viewModel, scrollState)
                        2 -> SymptomFrequencyPage(state, viewModel, scrollState)
                        3 -> NotesAndSummaryPage(state, viewModel, scrollState)
                    }
                }
            }

            // Barra de navegação inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.onEvent(FormsEvent.SaveDraft) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Salvar rascunho")
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botão de página anterior
                    IconButton(
                        onClick = { viewModel.onEvent(FormsEvent.PreviousPage) },
                        enabled = state.currentPage > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Página anterior",
                            tint = if (state.currentPage > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }

                    // Indicador de página
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        activeColor = MaterialTheme.colorScheme.primary,
                        inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )

                    // Botão de próxima página
                    IconButton(
                        onClick = { viewModel.onEvent(FormsEvent.NextPage) },
                        enabled = state.currentPage < state.totalPages - 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Próxima página",
                            tint = if (state.currentPage < state.totalPages - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StressLevelPage(
    state: FormsState,
    viewModel: FormsViewModel,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Como você classificaria seu nível de estresse atual?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "${state.stressLevel}",
            style = MaterialTheme.typography.displayLarge,
            color = when (state.stressLevel) {
                in 0..3 -> MaterialTheme.colorScheme.primary
                in 4..7 -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.error
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = state.stressLevel.toFloat(),
            onValueChange = { viewModel.onEvent(FormsEvent.StressLevelChanged(it.toInt())) },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0\nMuito baixo",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = "5\nModerado",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "10\nMuito alto",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = getStressLevelDescription(state.stressLevel),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun SymptomsSelectionPage(
    state: FormsState,
    viewModel: FormsViewModel,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Selecione os sintomas que você experimentou na última semana:",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "(Selecione todos que se aplicam)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        commonSymptoms.forEach { symptom ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.selectedSymptoms.contains(symptom),
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(FormsEvent.SymptomCheckedChanged(symptom, isChecked))
                    }
                )
                Text(
                    text = symptom,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SymptomFrequencyPage(
    state: FormsState,
    viewModel: FormsViewModel,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Com que frequência você experimenta os sintomas selecionados?",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (state.selectedSymptoms.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum sintoma selecionado na etapa anterior.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            state.selectedSymptoms.forEach { symptom ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = symptom,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val selectedFrequency = state.symptomFrequency[symptom] ?: SymptomFrequency.NEVER
                        
                        Column(modifier = Modifier.selectableGroup()) {
                            SymptomFrequency.values().forEach { frequency ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .selectable(
                                            selected = (frequency == selectedFrequency),
                                            onClick = {
                                                viewModel.onEvent(
                                                    FormsEvent.SymptomFrequencyChanged(
                                                        symptom, frequency
                                                    )
                                                )
                                            },
                                            role = Role.RadioButton
                                        )
                                        .padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (frequency == selectedFrequency),
                                        onClick = null
                                    )
                                    Text(
                                        text = getFrequencyText(frequency),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotesAndSummaryPage(
    state: FormsState,
    viewModel: FormsViewModel,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Notas adicionais e resumo",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Utilize este espaço para adicionar qualquer informação ou nota que considere relevante:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = state.notes,
            onValueChange = { viewModel.onEvent(FormsEvent.NotesChanged(it)) },
            label = { Text("Notas (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Resumo da autoavaliação",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Resumo do nível de estresse
        SummaryItem(
            title = "Nível de estresse:",
            value = "${state.stressLevel}/10",
            color = when (state.stressLevel) {
                in 0..3 -> MaterialTheme.colorScheme.primary
                in 4..7 -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.error
            }
        )

        // Resumo dos sintomas selecionados
        SummaryItem(
            title = "Sintomas relatados:",
            value = "${state.selectedSymptoms.size}",
            color = when (state.selectedSymptoms.size) {
                0 -> MaterialTheme.colorScheme.primary
                in 1..3 -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.error
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Ao enviar este formulário, você concorda que suas respostas sejam utilizadas de forma anônima para fins de acompanhamento do bem-estar da equipe.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun SummaryItem(
    title: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )
}

// Funções auxiliares
fun getStressLevelDescription(level: Int): String {
    return when (level) {
        in 0..2 -> "Seu nível de estresse é muito baixo. Você está se sentindo relaxado e tranquilo."
        in 3..4 -> "Seu nível de estresse é baixo. Você está lidando bem com as pressões do dia a dia."
        in 5..6 -> "Seu nível de estresse é moderado. Você pode estar sentindo alguma pressão, mas ainda está administrável."
        in 7..8 -> "Seu nível de estresse é alto. Você pode estar enfrentando dificuldades para lidar com a pressão atual."
        else -> "Seu nível de estresse é muito alto. Considere buscar apoio ou implementar técnicas de gerenciamento de estresse."
    }
}

fun getFrequencyText(frequency: SymptomFrequency): String {
    return when (frequency) {
        SymptomFrequency.NEVER -> "Nunca"
        SymptomFrequency.RARELY -> "Raramente"
        SymptomFrequency.SOMETIMES -> "Às vezes"
        SymptomFrequency.FREQUENTLY -> "Frequentemente"
        SymptomFrequency.ALWAYS -> "Sempre"
    }
}

// Lista de sintomas comuns para seleção
val commonSymptoms = listOf(
    "Dificuldade para dormir",
    "Dores de cabeça",
    "Tensão muscular",
    "Fadiga excessiva",
    "Dificuldade de concentração",
    "Irritabilidade",
    "Ansiedade",
    "Mudanças de apetite",
    "Sentimentos de tristeza",
    "Preocupação constante",
    "Dores no corpo",
    "Palpitações",
    "Pensamentos acelerados",
    "Indecisão",
    "Procrastinação"
)
