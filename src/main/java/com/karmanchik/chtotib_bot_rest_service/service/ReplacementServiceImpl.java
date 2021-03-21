package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.exception.StringReadException;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaGroupRepository;
import com.karmanchik.chtotib_bot_rest_service.parser.ReplacementParser;
import com.karmanchik.chtotib_bot_rest_service.jpa.JpaReplacementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service("replacementServiceImpl")
@RequiredArgsConstructor
public class ReplacementServiceImpl implements ReplacementService {
    private final JpaReplacementRepository replacementRepository;
    private final JpaGroupRepository groupRepository;


    @Override
    public Replacement save(Replacement replacement) {
        return replacementRepository.save(replacement);
    }

    @Override
    public Replacement findById(Integer id) throws ResourceNotFoundException {
        return replacementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Replacement.class));
    }

    @Override
    public List<Replacement> findAll() {
        return replacementRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
    }

    @Override
    public void deleteById(Integer id) {
        replacementRepository.deleteById(id);
    }

    @Override
    public void delete(Replacement replacement) {
        replacementRepository.delete(replacement);
    }

    @Override
    public Object save(MultipartFile file) throws StringReadException, IOException {
        InputStream stream = file.getInputStream();
        String jsonStr = new ReplacementParser(stream).parse();
        JSONArray json = new JSONArray(jsonStr);

        List<Group> groups = groupRepository.findAll();

        for (Object o : json) {
            JSONObject item = (JSONObject) o;
            log.info("Get item - {}", item);
            String groupName = item.getString("group_name");

            LocalDate date = LocalDate.parse(item.getString("date"));
            String lessons = item.getJSONArray("lessons").toString();

            groups.forEach(group -> {
                if (group.getGroupName().equalsIgnoreCase(groupName)) {
                    replacementRepository.findByGroupAndDate(group, date)
                            .orElseGet(() -> replacementRepository.save(
                                    Replacement.builder()
                                            .group(group)
                                            .lessons(lessons)
                                            .date(date)
                                            .build()
                            ));
                }
            });
        }
        stream.close();
        return json.toString();
    }
}
