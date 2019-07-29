package top.toptimus.merkle.baseModel;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import top.toptimus.constantConfig.Constants;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.place.PlaceDTO;
import top.toptimus.timer.LogExecuteTime;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * merkle基类，保存：
 * 主表信息map（meta id，表名，sql）
 * token索引
 * token byte数据
 *
 * @author gaoyu
 * @since 2019-01-04
 */
public abstract class MerkleBaseModel implements Serializable, Runnable {
    private static final long serialVersionUID = -8450832400591234279L;
    //长度8字节
    private final static String DES_KEY = "12345678";
    private final static String ENCRYPT_METHOD = "DES";
    @Getter
    protected Map<String, MainTable> mainTableMap = new HashMap<>();  //  K:meta下type页的id   ,V:主表信息，带sql
    @Getter
    protected Map<String, List<String>> tokenIndex = new HashMap<>();   //  K: metaId, V: token id list
    private Map<String, byte[]> tokenDatas = new HashMap<>();   //  k：token id，V:token data byte

    /**
     * 工作包数据比对实体(top.toptimus.merkle.MerkleWorkPackageModel)中使用
     *
     * @param placeDTO 库所
     */
    public MerkleBaseModel(PlaceDTO placeDTO) {
        placeDTO.getDatas().keySet().forEach(metaId -> placeDTO.getDatas().get(metaId).forEach(tokenDataDto -> {
            {
                try {
                    this.tokenDatas.put(tokenDataDto.getTokenId(), MerkleBaseModel.encByDes(tokenDataDto));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.buildIndex(metaId, tokenDataDto);  //  建立索引
            }
        }));
    }

    /**
     * MainTable的缓存
     *
     * @param mainTableMap mainTableMap
     */
    public MerkleBaseModel(Map<String, MainTable> mainTableMap) {
        this.mainTableMap.putAll(mainTableMap);
    }

    /**
     * 分页方法
     *
     * @param tokenDataDtos token数据
     * @param fKey          fkey
     * @param pageSize      页宽
     * @param pageNo        页码
     * @return token id列表
     */
    private static List<String> getTokensOrder(
            List<TokenDataDto> tokenDataDtos
            , String fKey
            , int pageSize
            , int pageNo
    ) {
        if (StringUtils.isEmpty(fKey)) {
            // 默认按照lotno排序
            fKey = Constants.lot_no;
        }
        int fromIndex = pageSize * (pageNo - 1);
        int toIndex = pageSize * pageNo - 1 > tokenDataDtos.size() ? tokenDataDtos.size() : pageSize * pageNo - 1;
        List<Map.Entry<String, String>> tokenList = new ArrayList<>(
                MerkleBaseModel.convertToMap(tokenDataDtos, fKey).entrySet()
        );
        tokenList.sort((o1, o2) -> (o1.getValue().compareTo(o2.getValue())));

        return new ArrayList<String>() {
            private static final long serialVersionUID = 3171271930568879730L;

            {
                tokenList.subList(fromIndex, toIndex).forEach(tokenEntry -> add(tokenEntry.getKey()));
            }
        };
    }

    /**
     * 根据fkey从token列表中提取token id和json数据的map
     *
     * @param tokenDataDtos token列表
     * @param fKey          fkey
     * @return token id和json数据的map
     */
    private static Map<String, String> convertToMap(List<TokenDataDto> tokenDataDtos, String fKey) {
        return new HashMap<String, String>() {
            private static final long serialVersionUID = -3093463397150187131L;

            {
                for (TokenDataDto tokenDataDto : tokenDataDtos) {
                    for (FkeyField fkeyField : tokenDataDto.getFields()) {
                        if (fKey.equals(fkeyField.getKey())) {
                            put(tokenDataDto.getTokenId(), fkeyField.getJsonData());
                            break;
                        }
                    }
                }
            }
        };
    }

    private static byte[] decByDes(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(MerkleBaseModel.ENCRYPT_METHOD);
        cipher.init(
                Cipher.DECRYPT_MODE
                , SecretKeyFactory
                        .getInstance(MerkleBaseModel.ENCRYPT_METHOD)
                        .generateSecret(
                                new DESKeySpec(MerkleBaseModel.DES_KEY.getBytes())
                        )
                , new SecureRandom()    // DES算法要求有一个可信任的随机数源
        );
        return cipher.doFinal(data);
    }

    private static byte[] encByDes(TokenDataDto obj) throws Exception {
        byte[] hash = null;
        if (obj != null) {
            Cipher cipher = Cipher.getInstance(MerkleBaseModel.ENCRYPT_METHOD);
            cipher.init(
                    Cipher.ENCRYPT_MODE
                    , SecretKeyFactory
                            .getInstance(MerkleBaseModel.ENCRYPT_METHOD)
                            .generateSecret(
                                    new DESKeySpec(MerkleBaseModel.DES_KEY.getBytes())
                            )   //  密钥
            );
            hash = cipher.doFinal(
                    MerkleBaseModel.getBytes(obj)   //  bytes
            );
        }
        return hash;
    }

    private static byte[] hash(TokenDataDto tokenDataDto) {
        byte[] hash = null;
        if (tokenDataDto != null) {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            hash = digest.digest(MerkleBaseModel.getBytes(tokenDataDto));
        }
        return hash;
    }

    private static byte[] getBytes(Serializable obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
        } catch (IOException e) {
            // nothing recoverable; throw a runtime exception:
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    /**
     * 建立索引
     *
     * @param metaId       meta id
     * @param tokenDataDto token数据
     */
    private void buildIndex(String metaId, TokenDataDto tokenDataDto) {
        if (this.tokenIndex.containsKey(metaId)) {
            if (!this.tokenIndex.get(metaId).contains(tokenDataDto.getTokenId())) {
                this.tokenIndex.get(metaId).add(tokenDataDto.getTokenId());
            }
        } else {
            tokenIndex.put(metaId, Lists.newArrayList(tokenDataDto.getTokenId()));
        }
    }


    /**
     * 根据metaId获取TokenDataDtos
     *
     * @param metaId meta id
     * @return token数据
     */
    public List<TokenDataDto> getTokenDataDtosByMetaId(String metaId, int pageSize, int pageNo, String fkey) {
        List<TokenDataDto> tokenDataDtos = new ArrayList<>();
        this.tokenIndex.get(metaId).forEach(tokenId -> {
            try {
                tokenDataDtos.add(this.getTokenDataDto(tokenId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        List<TokenDataDto> returnTokenDataDtos = new ArrayList<>();
        MerkleBaseModel.getTokensOrder(tokenDataDtos, fkey, pageSize, pageNo).forEach(newTokenId -> {
            try {
                returnTokenDataDtos.add(this.getTokenDataDto(newTokenId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return returnTokenDataDtos;
    }

    /**
     * 根据metaId获取TokenDataDtos
     *
     * @param metaId meta id
     * @return token数据
     */
    public List<TokenDataDto> getTokenDataDtosByMetaIdAndTokenId(String metaId, List<String> tokenIds) {
        List<TokenDataDto> tokenDataDtos = new ArrayList<>();
        this.tokenIndex.get(metaId).forEach(tokenId -> {
            try {
                if (tokenIds.contains(tokenId)) {
                    tokenDataDtos.add(this.getTokenDataDto(tokenId));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return tokenDataDtos;
    }

    /**
     * 检验库所数据merkle diff并返回
     *
     * @param datas 数据，K: meta id , V: token list
     * @return diff token id list
     */
    public Map<String, List<String>> alterPlaceDatas(Map<String, List<TokenDataDto>> datas) {
        return new HashMap<String, List<String>>() {
            {
                datas.keySet().forEach(metaId -> put(metaId, alter(metaId, datas.get(metaId))));
            }
        };
    }

    /**
     * 整合数据并返回diff
     *
     * @param tokenDataDtos token数据列表
     * @return diff token id list
     */
    @LogExecuteTime
    private List<String> alter(String metaId, List<TokenDataDto> tokenDataDtos) {
        List<String> diffs = new ArrayList<>(); //  不一致的数据
        tokenDataDtos.forEach(tokenDataDto -> {
            this.buildIndex(metaId, tokenDataDto);  //  建立索引
            if (!this.tokenDatas.containsKey(tokenDataDto.getTokenId())) {  //  新增
                diffs.add(tokenDataDto.getTokenId());
                this.tokenDatas.put(tokenDataDto.getTokenId(), MerkleBaseModel.hash(tokenDataDto));  //  追加数据
//                System.out.println(tokenDataDto.getTokenId() + ",新增");
            } else {
                byte[] obj = MerkleBaseModel.hash(tokenDataDto);
                if (!Arrays.equals(this.tokenDatas.get(tokenDataDto.getTokenId()), obj)) {  //  变更
                    diffs.add(tokenDataDto.getTokenId());
                    this.tokenDatas.put(tokenDataDto.getTokenId(), obj);    //  追加数据
//                    System.out.println(tokenDataDto.getTokenId() + ",追加");
                }
            }
        });
        return diffs;
    }

    /**
     * 取得变更/新增的数据
     *
     * @param metaId        meta id
     * @param tokenDataDtos token数据
     * @return K: meta id, V: token数据列表
     */
    @LogExecuteTime
    public Map<String, List<TokenDataDto>> alterTokenData(String metaId, List<TokenDataDto> tokenDataDtos) {
        List<TokenDataDto> diffs = new ArrayList<>(); //  不一致的数据
        tokenDataDtos.forEach(tokenDataDto -> {
            this.buildIndex(metaId, tokenDataDto);  //  建立索引
            if (!this.tokenDatas.containsKey(tokenDataDto.getTokenId())) {  //  新增
                diffs.add(tokenDataDto);
                this.tokenDatas.put(tokenDataDto.getTokenId(), MerkleBaseModel.hash(tokenDataDto));  //  追加数据
//                System.out.println(tokenDataDto.getTokenId() + ",新增");
            } else {
                byte[] obj = new byte[0];
                try {
                    obj = MerkleBaseModel.encByDes(tokenDataDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!Arrays.equals(this.tokenDatas.get(tokenDataDto.getTokenId()), obj)) {  //  变更
                    diffs.add(tokenDataDto);
                    this.tokenDatas.put(tokenDataDto.getTokenId(), obj);    //  追加数据
//                    System.out.println(tokenDataDto.getTokenId() + ",追加");
                }
            }
        });
        return new HashMap<String, List<TokenDataDto>>() {
            private static final long serialVersionUID = 4650607200192858671L;

            {
                put(metaId, diffs);
            }
        };
    }

    public String delete(String metaId, List<TokenDataDto> tokenDataDtos) {
        tokenDataDtos.forEach(tokenDataDto -> {
            this.tokenIndex.get(metaId).forEach(tokenId -> {
                if (tokenId.equals(tokenDataDto.getTokenId())) {
                    this.tokenIndex.get(metaId).remove(tokenId);
                }
            });
            //  删除token
            this.tokenDatas.remove(tokenDataDto.getTokenId());  //  追加数据
        });
        if (this.tokenIndex.get(metaId) != null && this.tokenIndex.get(metaId).size() > 0) {
            return null;
        } else {
            this.tokenIndex.remove(metaId);
            return metaId;
        }
    }

    /**
     * 从byte中取token数据
     *
     * @param tokenId token id
     * @return token数据
     * @throws Exception Exception
     */
    protected TokenDataDto getTokenDataDto(String tokenId) throws Exception {
        ByteArrayInputStream byteArrayInStream = new ByteArrayInputStream(MerkleBaseModel.decByDes(this.tokenDatas.get(tokenId)));
        ObjectInputStream objectInStream = new ObjectInputStream(byteArrayInStream);
        TokenDataDto tokenDataDto = (TokenDataDto) objectInStream.readObject();
        byteArrayInStream.close();
        objectInStream.close();

        return tokenDataDto;
    }

    @Override
    public void run() {

    }
}
