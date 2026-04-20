package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.model.WatchList;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface WatchListMapper {

    WatchListResponseDto toDto(WatchList watchList);

    List<WatchListResponseDto> toDtoList(List<WatchList> watchList);
}
