package spring.demo.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.demo.model.Task;
import spring.demo.model.TaskGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempService {

    @Autowired
    List<String> temp(TaskGroupRepository repository){
        // FIXME: N + 1
      return repository.findAll()
                .stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getDescription)
                .collect(Collectors.toList());
    }
}
