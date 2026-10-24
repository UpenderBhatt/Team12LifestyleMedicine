const reflectionModal = document.getElementById("reflectionModal");
const closeReflectionModal = document.getElementById("closeReflectionModal");
const saveReflectionBtn = document.getElementById("saveReflectionBtn");
const editReflectionBtn = document.getElementById("editReflectionBtn");
const completeCheckbox = document.getElementById("completeCheckbox");
const reflectionText = document.getElementById("reflectionText");
const modalTitle = document.getElementById("modalTitle");
const modalGoalDesc = document.getElementById("modalGoalDesc");

const currentUserId = Number(document.body.dataset.userId);

//Variables storing csrf token and header
const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

let activeTaskId = null;
let activeCell = null;
let hasReflection = false;

document.querySelectorAll(".roadmap-cell").forEach(cell => {
    cell.addEventListener("click", async () => {

        if (cell.classList.contains("cell-future")) return;

        const title = cell.dataset.taskTitle;
        modalTitle.innerText = title;
        modalGoalDesc.innerText = cell.dataset.taskDesc;

        activeCell = cell;
        activeTaskId = cell.dataset.taskId;

        reflectionText.classList.add("hidden");
        saveReflectionBtn.classList.add("hidden");
        editReflectionBtn.classList.add("hidden");
        completeCheckbox.parentElement.style.display = "flex";
        reflectionText.disabled = true;
        hasReflection = false;

        const response = await fetch(`/api/reflections/task/${activeTaskId}`);
        if (response.ok) {
            const data = await response.json();
            if (data.exists) {
                hasReflection = true;
                reflectionText.value = data.content;
            } else {
                reflectionText.value = "";
            }
        }

        const isMissed = cell.classList.contains("cell-past-incomplete");
        if (isMissed) {
            completeCheckbox.parentElement.style.display = "none";
            reflectionText.classList.remove("hidden");
            reflectionText.disabled = false;

            if (hasReflection) {
                editReflectionBtn.classList.remove("hidden");
            } else {
                saveReflectionBtn.classList.remove("hidden");
            }

            reflectionModal.style.display = "block";
            return;
        }

        completeCheckbox.checked = cell.classList.contains("cell-complete");
        if (completeCheckbox.checked) {
            reflectionText.classList.remove("hidden");
            if (!hasReflection) {
                reflectionText.disabled = false;
                saveReflectionBtn.classList.remove("hidden");
            } else {
                editReflectionBtn.classList.remove("hidden");
            }
        }

        reflectionModal.style.display = "block";
    });
});

completeCheckbox.addEventListener("change", async () => {
    if (!activeCell) return;

    const isComplete = completeCheckbox.checked;
    const newStatus = isComplete ? "Completed" : "Pending";

    await fetch(`/api/tasks/${activeTaskId}/status`, {
        method: "POST",
        headers: { "Content-Type": "application/json", [csrfHeader]: csrfToken},
        body: JSON.stringify({ status: newStatus })
    });

    activeCell.classList.remove(
        "cell-today",
        "cell-complete",
        "cell-past-incomplete",
        "cell-future"
    );

    if (isComplete) {
        activeCell.classList.add("cell-complete");
        reflectionText.classList.remove("hidden");

        if (!hasReflection) {
            reflectionText.disabled = false;
            saveReflectionBtn.classList.remove("hidden");
        }

    } else {
        activeCell.classList.add("cell-today");
        reflectionText.classList.add("hidden");
        saveReflectionBtn.classList.add("hidden");
        editReflectionBtn.classList.add("hidden");
    }
});

editReflectionBtn.onclick = () => {
    reflectionText.disabled = false;
    editReflectionBtn.classList.add("hidden");
    saveReflectionBtn.classList.remove("hidden");
};

saveReflectionBtn.onclick = async () => {
    const content = reflectionText.value.trim();
    if (!content) return;

    await fetch("/api/reflections/save", {
        method: "POST",
        headers: { "Content-Type": "application/json", [csrfHeader]: csrfToken},
        body: JSON.stringify({
            userId: currentUserId,
            taskId: activeTaskId,
            content
        })
    });

    reflectionText.disabled = true;
    saveReflectionBtn.classList.add("hidden");
    editReflectionBtn.classList.remove("hidden");
    hasReflection = true;

    reflectionModal.style.display = "none";
};

closeReflectionModal.onclick = () => reflectionModal.style.display = "none";

window.onclick = (e) => {
    if (e.target === reflectionModal) reflectionModal.style.display = "none";
};

const nextBtn = document.getElementById("nextDayBtn");
const prevBtn = document.getElementById("prevDayBtn");

if (nextBtn) {
    nextBtn.onclick = async () => {
        await fetch("/test/next-day");
        location.reload();
    };
}

if (prevBtn) {
    prevBtn.onclick = async () => {
        await fetch("/test/previous-day");
        location.reload();
    };
}
