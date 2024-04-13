package com.kickalert.app.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@Slf4j
public class RepositorySliceHelper {
    public static <T> Slice<T> toSlice(List<T> contents, Pageable pageable){
        boolean hasNext = isContentSizeGreaterThanPageSize(contents, pageable);
        log.info("hasNext: {}", hasNext);
        return new SliceImpl<>(hasNext ? getSubListLastContent(contents, pageable) : contents, pageable, hasNext);
    }

    private static <T> List<T> getSubListLastContent(List<T> contents, Pageable pageable) {
        return contents.subList(0, pageable.getPageSize());
    }

    private static <T> boolean isContentSizeGreaterThanPageSize(List<T> contents, Pageable pageable) {
        return pageable.isPaged() && contents.size() > pageable.getPageSize();
    }
}
