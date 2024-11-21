package dgp.misaeng.global.exception;

public class S3Exception extends CustomException{
    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }

}
