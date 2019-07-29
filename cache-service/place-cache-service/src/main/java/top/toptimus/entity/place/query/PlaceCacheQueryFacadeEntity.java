package top.toptimus.entity.place.query;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.dao.place.TokenRedisDao;
import top.toptimus.place.PlaceDTO;
import top.toptimus.repository.place.MemorandumRedisRepository;
import top.toptimus.repository.place.PlaceRedisRepository;
import top.toptimus.repository.place.TokenRedisRepository;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 库所缓存查询实体
 *
 * @author gaoyu
 */
@Component
public class PlaceCacheQueryFacadeEntity {
    @Autowired
    private PlaceRedisRepository placeRepository;
    @Autowired
    private TokenRedisRepository tokenRepository;
    @Autowired
    private MemorandumRedisRepository memorandumRepository;

    /**
     * 判定tokenIds是否上锁
     *
     * @param tokenIds token列表
     * @return true未锁 false已锁
     */
    public boolean checkTokenExist(List<String> tokenIds) {
        try {
            List<TokenRedisDao> tokenRedisDaos = Lists.newArrayList(tokenRepository.findAllById(tokenIds));
            for (TokenRedisDao dao : tokenRedisDaos) {
                if (dao.isLock()) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 根据lotNo取得备查账的信息
     *
     * @param lotNo lot no
     * @return 备查账对应的tokenIds
     */
    public ArrayList<TokenRedisDao> findMemorandumsDaoById(String lotNo) {
        try {
            return new ArrayList<TokenRedisDao>() {{
                memorandumRepository.findById(lotNo).ifPresent(memorandumsRedisDao -> memorandumsRedisDao.getTokenIds().forEach(tokenId -> add(new TokenRedisDao(tokenId))));
            }};
        } catch (Exception e) {
            throw new RuntimeException("根据lotNo取得备查账信息失败");
        }
    }

    /**
     * 根据tokenIds查询redis中TokenDataDtos
     *
     * @param tokenIds token id列表
     * @return token数据列表
     */
    public List<TokenDataDto> findAllTokenDataDtoByIds(List<String> tokenIds) {
        return new ArrayList<TokenDataDto>() {{
            Lists.newArrayList(tokenRepository.findAllById(tokenIds)).forEach(tokenRedisDao -> add(tokenRedisDao.getTokenDataDto()));
        }};
    }

    /**
     * 根据lotNo查找指定PlaceDTO
     *
     * @param lotNo 标识多条备查帐启动相同流程的组ID
     * @return 库所
     */
    public PlaceDTO findPlaceDTOById(String lotNo) {
        try {
            return placeRepository.findByLotNo(lotNo).get(0).convertPlaceDTO();
        } catch (Exception e) {
            e.printStackTrace();
            return null; //未取到返回
        }
    }
}
